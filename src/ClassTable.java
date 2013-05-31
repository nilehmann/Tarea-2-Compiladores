import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * This class may be used to contain the semantic information such as the
 * inheritance graph. You may use it or not as you like: it is only here to
 * provide a container for the supplied methods.
 */
class ClassTable {
	private Map<String, Class_> classes;
	private Map<String, String> tree;
	private Map<String, ArrayList<String> > invTree;

	private int semantErrors;
	private PrintStream errorStream;

	/**
	 * Creates data structures representing basic Cool classes (Object, IO, Int,
	 * Bool, String). Please note: as is this method does not do anything
	 * useful; you will need to edit it to make if do what you want.
	 * */
	private void bassicClasses() {

		// The Object class has no parent class. Its methods are
		// cool_abort() : Object aborts the program
		// type_name() : Str returns a string representation
		// of class name
		// copy() : SELF_TYPE returns a copy of the object

		Class_ Object_class = new Class_(0, "Object", "No_class",
				new Features(0)
						.appendElement(
								new method(0, "abort", new Formals(0),
										"Object", new no_expr(0)))
						.appendElement(
								new method(0, "type_name", new Formals(0),
										"String", new no_expr(0)))
						.appendElement(
								new method(0, "copy", new Formals(0),
										"SELF_TYPE", new no_expr(0))),
				"<basic class>");

		// The IO class inherits from Object. Its methods are
		// out_string(Str) : SELF_TYPE writes a string to the output
		// out_int(Int) : SELF_TYPE "    an int    " "     "
		// in_string() : Str reads a string from the input
		// in_int() : Int "   an int     " "     "

		Class_ IO_class = new Class_(0, "IO", "Object", new Features(0)
				.appendElement(
						new method(0, "out_string", new Formals(0)
								.appendElement(new Formal(0, "arg", "String")),
								"SELF_TYPE", new no_expr(0)))
				.appendElement(
						new method(0, "out_int", new Formals(0)
								.appendElement(new Formal(0, "arg", "Int")),
								"SELF_TYPE", new no_expr(0)))
				.appendElement(
						new method(0, "in_string", new Formals(0), "String",
								new no_expr(0)))
				.appendElement(
						new method(0, "in_int", new Formals(0), "Int",
								new no_expr(0))), "<basic class>");

		// The Int class has no methods and only a single attribute, the
		// "val" for the integer.

		Class_ Int_class = new Class_(0, "Int", "Object",
				new Features(0).appendElement(new attr(0, "_val", "_prim_slot",
						new no_expr(0))), "<basic class>");

		// Bool also has only the "val" slot.
		Class_ Bool_class = new Class_(0, "Bool", "Object",
				new Features(0).appendElement(new attr(0, "_val", "_prim_slot",
						new no_expr(0))), "<basic class>");

		// The class Str has a number of slots and operations:
		// val the length of the string
		// str_field the string itself
		// length() : Int returns length of the string
		// concat(arg: Str) : Str performs string concatenation
		// substr(arg: Int, arg2: Int): Str substring selection

		Class_ Str_class = new Class_(0, "String", "Object",
				new Features(0)
						.appendElement(
								new attr(0, "_val", "Int", new no_expr(0)))
						.appendElement(
								new attr(0, "_str_field", "_prim_slot",
										new no_expr(0)))
						.appendElement(
								new method(0, "length", new Formals(0), "Int",
										new no_expr(0)))
						.appendElement(
								new method(0, "concat", new Formals(0)
										.appendElement(new Formal(0, "arg",
												"String")), "String",
										new no_expr(0)))
						.appendElement(
								new method(0, "substr", new Formals(0)
										.appendElement(
												new Formal(0, "arg", "Int"))
										.appendElement(
												new Formal(0, "arg2", "Int")),
										"Strring", new no_expr(0))),
				"<basic class>");

		classes.put("Object", Object_class);
		classes.put("IO", IO_class);
		classes.put("Int", Int_class);
		classes.put("Bool", Bool_class);
		classes.put("String", Str_class);
		
		tree.put("Object", null);
		tree.put("IO", "Object");
		tree.put("Int", "Object");
		tree.put("Bool", "Object");
		tree.put("String", "Object");
		
		invTree.put("Object", new ArrayList<String>());
		invTree.put("IO", new ArrayList<String>());
		invTree.put("Int", new ArrayList<String>());
		invTree.put("Bool", new ArrayList<String>());
		invTree.put("String", new ArrayList<String>());
		
		invTree.get("Object").add("IO");
		invTree.get("Object").add("Int");
		invTree.get("Object").add("Bool");
		invTree.get("Object").add("String");

	}

	private void buildGraph(Classes cls) {
		classes = new HashMap<String, Class_>();
		tree = new HashMap<String, String>();
		invTree = new HashMap<String, ArrayList<String> >();

		bassicClasses();
		
		for (Enumeration<TreeNode> e = cls.getElements(); e.hasMoreElements();) {
			Class_ c = (Class_) e.nextElement();
			String name = c.name;
			
			if(classes.containsKey(name)){
				System.err.println("Class redefinition at: "+c.getLineNumber());
				System.exit(0);
			}
			
			classes.put(name, c);
			invTree.put(name, new ArrayList<String>());
			
		}
		UnionFind uf = new UnionFind();
		uf.union("Object", "IO");
		uf.union("Object", "Int");
		uf.union("Object", "Bool");
		uf.union("Object", "String");
		
		
		for (Enumeration<TreeNode> e = cls.getElements(); e.hasMoreElements();) {
			Class_ c = (Class_) e.nextElement();
			String name = c.name;
			String parent = c.parent;
			
			if(parent.equals("Int") || parent.equals("String") || parent.equals("Bool")){
				System.err.println("Cannot inherit from: "+parent);
				System.exit(0);
			}
				
			
			if(!classes.containsKey(parent)){
				System.err.println("Missing parent class: "+parent);
				System.exit(0);
			}
			
			tree.put(name, parent);
			invTree.get(parent).add(name);
			uf.union(name, parent);
		}
		
		
		if(uf.getMaxRank() < 5 + cls.getLength()){
			System.err.println("Inheritance graph contains cycles");
			System.exit(0);
		}
			
	
		
	}
	

	private void checkOverride(){
		for(Class_ cl : classes.values()){
			if(cl.name.equals("Object"))
				continue;
			Features fts = cl.features;		
			for (Enumeration<TreeNode> e = fts.getElements(); e.hasMoreElements();) {
				TreeNode f = e.nextElement();
				if(f instanceof attr){
					attr at = (attr) f;
					if( attrLookup(tree.get(cl.name), at.name) != null){
						System.err.println("Class redefines attribute.");
						System.exit(0);
					}
				}
				else if(f instanceof method){
					method m = (method) f;
					
					method m2 = methodLookup(tree.get(cl.name), m.name);
					
					if(m2 != null && !m.compSignature(m2)){
						System.err.println("Bad method overriding");
						System.exit(0);
					}
						
				}
				else{
					System.err.println("Unexpected error");
					System.exit(0);
				}
			}
		}
	}
	
	public String lca(AbstractType class1, AbstractType class2){
		return "hola";
	}
	
	public method getMethod(String strClass, String methodName){
		Class_ cl = classes.get(strClass);		
		
		Features fts = cl.features;		
		for (Enumeration<TreeNode> e = fts.getElements(); e.hasMoreElements();) {
			TreeNode f = e.nextElement();
			if(f instanceof attr){}
			else if(f instanceof method){
				method m = (method) f;
				if(m.name.equals(methodName))
					return m;
			}
			else{
				System.err.println("Unexpected error");
				System.exit(0);
			}
		}
		return null;
	}
	
	public method methodLookup(String strClass, String methodName){
		method m = getMethod(strClass, methodName);
		if(m != null)
			return m;

		if(strClass.equals("Object"))
			return null;
		return methodLookup(tree.get(strClass), methodName);
	}
	

	public attr attrLookup(String strClass, String attrName){
		Class_ cl = classes.get(strClass);
		
		Features fts = cl.features;		
		for (Enumeration<TreeNode> e = fts.getElements(); e.hasMoreElements();) {
			TreeNode f = e.nextElement();
			if(f instanceof attr){
				attr at = (attr) f;
				if(at.name.equals(attrName))
					return at;
			}
			else if(f instanceof method){}
			else{
				System.err.println("Unexpected error");
				System.exit(0);
			}
		}

		if(strClass.equals("Object"))
			return null;
		return attrLookup(tree.get(strClass), attrName);
	}
	
	public boolean subType(String T1, String T2){
		if(T1 == null)
			return false;

		
		
		if(T1.equals(T2))
			return true;
		
		return subType(tree.get(T1), T2);
	}
	
	public ClassTable(Classes cls) {
		semantErrors = 0;
		errorStream = System.err;
		buildGraph(cls);
		checkOverride();
	}

	/**
	 * Prints line number and file name of the given class.
	 * 
	 * Also increments semantic error count.
	 * 
	 * @param c
	 *            the class
	 * @return a print stream to which the rest of the error message is to be
	 *         printed.
	 * 
	 * */
	public PrintStream semantError(Class_ c) {
		return semantError(c.getFilename(), c);
	}

	/**
	 * Prints the file name and the line number of the given tree node.
	 * 
	 * Also increments semantic error count.
	 * 
	 * @param filename
	 *            the file name
	 * @param t
	 *            the tree node
	 * @return a print stream to which the rest of the error message is to be
	 *         printed.
	 * 
	 * */
	public PrintStream semantError(String filename, TreeNode t) {
		errorStream.print(filename + ":" + t.getLineNumber() + ": ");
		return semantError();
	}

	/**
	 * Increments semantic error count and returns the print stream for error
	 * messages.
	 * 
	 * @return a print stream to which the error message is to be printed.
	 * 
	 * */
	public PrintStream semantError() {
		semantErrors++;
		return errorStream;
	}

	/** Returns true if there are any static semantic errors. */
	public boolean errors() {
		return semantErrors != 0;
	}
}
