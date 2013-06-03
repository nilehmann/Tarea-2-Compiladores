package cool.compiler;
import java.util.ArrayList;

abstract class AbstractType{
	abstract public boolean subType(AbstractType a, ClassTable classTable);
	abstract public String getClassForLookup();
	
	abstract public boolean equals(AbstractType type);
	
	public boolean equals(String type){
		return this.equals(new Type(type));
	}
	
	abstract public AbstractType lca(AbstractType t, ClassTable classTable);
}

class Type extends AbstractType{
	protected String type;
	
	public Type(String t){
		type = t;
	}
	
	@Override
	public boolean subType(AbstractType a, ClassTable classTable) {
		if(a instanceof Type)
			return classTable.subType(type,((Type)a).type);
		return false;
	}
	
	@Override
	public String toString(){
		return type;
	}

	@Override
	public String getClassForLookup() {
		return type;
	}


	@Override
	public boolean equals(AbstractType t) {
		if(!(t instanceof Type))
			return false;

		
		return ((Type) t).type.equals(type);
	}
	
	@Override
	public AbstractType lca(AbstractType t, ClassTable classTable){
		if(t instanceof Type)
			return new Type(classTable.lca(type, ((Type) t).type));
		
		return t.lca(this, classTable);
	}
	
}

class SelfType extends AbstractType{
	protected String class_;
	
	public SelfType(String c){
		class_ = c;
	}

	@Override
	public boolean subType(AbstractType a, ClassTable classTable) {
		if( a instanceof SelfType)
			return class_.equals(((SelfType)a).class_);
		if( a instanceof Type){
			return classTable.subType(class_, ((Type) a).type);
		}
		
		
		return false;
	}
	
	@Override
	public String toString(){
		return "SELF_TYPE";
	}

	@Override
	public String getClassForLookup() {
		return class_;
	}

	@Override
	public boolean equals(AbstractType t) {
		if(!(t instanceof SelfType))
			return false;
		
		return ((SelfType) t).class_.equals(class_);
	}
	
	@Override
	public AbstractType lca(AbstractType t, ClassTable classTable){
		if(t instanceof Type)
			return new Type(classTable.lca(class_, ((Type)t).type));
		if(t instanceof SelfType && ((SelfType) t).class_.equals(class_))
			return this;
				
		return new Type("Object");
	}
}

class NoType extends AbstractType{

	@Override
	public String getClassForLookup() {
		Utilities.fatalError("Unexpected type");
		return null;
	}

	@Override
	public boolean subType(AbstractType a, ClassTable classTable) {
		return true;
	}
	
	@Override
	public String toString(){
		return "_no_type";
	}

	@Override
	public boolean equals(AbstractType type) {
		return type instanceof NoType;
	}

	@Override
	public AbstractType lca(AbstractType t, ClassTable classTable) {
		return new Type("Object");
	}
	
}



class ListType{
	ArrayList<AbstractType> list;
	
	public ListType(){
		list = new ArrayList<AbstractType>();
	}
	
	public void addType(AbstractType t){
		list.add(t);
	}

	public int size(){
		return list.size();
	}
	
	public AbstractType get(int i){
		return list.get(i);
	}
	public AbstractType last(){
		return list.get(list.size()-1);
	}

	public boolean subType(ListType t2, ClassTable classTable) {
		if(list.size() != t2.list.size())
			return false;
		
		for (int i = 0; i < list.size(); i++) {
			AbstractType T1 = list.get(i);
			AbstractType T2 = t2.list.get(i);
			
			if(!T1.subType(T2, classTable))
				return false;
		}
		
		return true;
	}

	public boolean equals(ListType t2) {
		if(list.size() != t2.list.size())
			return false;
		
		for (int i = 0; i < list.size(); i++) {
			AbstractType T1 = list.get(i);
			AbstractType T2 = t2.list.get(i);
			
			if(!T1.equals(T2))
				return false;
		}
		
		return true;
	}
	
	public AbstractType lca(ClassTable classTable){
		if(list.size() == 0)
			return null;
		if(list.size() == 1)
			return list.get(0);
		
		AbstractType ret = list.get(0).lca(list.get(1), classTable);
		
		for (int i = 2; i < list.size(); i++)
			ret = ret.lca(list.get(i), classTable);
		
		return ret;
	}
	
	@Override
	public String toString(){
		return list.toString();
	}

}