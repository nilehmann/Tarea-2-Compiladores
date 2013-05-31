import java.util.ArrayList;
import java.util.Collections;

abstract class AbstractType{
	abstract public boolean subType(AbstractType a, ClassTable classTable);
	abstract public String getClassForLookup();
	public boolean equals(String type){
		return false;
	}
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
	public boolean equals(String strType) {
		return type.equals(strType);
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
		
		if( a instanceof Type)
			return classTable.subType(class_, ((Type) a).type);
		
		
		return false;
	}
	
	@Override
	public String toString(){
		return "SELF_TYPE_"+class_;
	}

	@Override
	public String getClassForLookup() {
		return class_;
	}
	
}

class ListType extends AbstractType{
	ArrayList<AbstractType> list;
	
	public ListType(){
		list = new ArrayList<AbstractType>();
	}
	
	public void addType(AbstractType t){
		list.add(t);
	}
	
	public AbstractType last(){
		return list.get(list.size()-1);
	}

	@Override
	public boolean subType(AbstractType a, ClassTable classTable) {
		if(a instanceof ListType){
			ListType t2 = (ListType) a;
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
		
		return false;
	}
	
	@Override
	public String toString(){
		return list.toString();
	}

	@Override
	public String getClassForLookup() {
		Utilities.fatalError("Unexpected type");
		return null;
	}
}