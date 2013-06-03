package cool.compiler;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



/**
 * This class may be used to contain the semantic information such as the
 * inheritance graph. You may use it or not as you like: it is only here to
 * provide a container for the supplied methods.
 */
class ClassTable {
	private Map<String, Class_> classes;
	private Map<String, String> tree;

	private int semantErrors;
	private PrintStream errorStream;

	public ClassTable(Classes cls) {
		semantErrors = 0;
		errorStream = System.err;
		buildGraph(cls);
		if (!classes.containsKey("Main"))
			abort("Class Main is not defined.");
		checkOverride();
	}

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

		Class_ Object_class = new Class_(0, "Object", "No_class", new Features(
				0)
				.appendElement(
						new method(0, "abort", new Formals(0), "Object",
								new no_expr(0)))
				.appendElement(
						new method(0, "type_name", new Formals(0), "String",
								new no_expr(0)))
				.appendElement(
						new method(0, "copy", new Formals(0), "SELF_TYPE",
								new no_expr(0))), "<basic class>");

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
										"String", new no_expr(0))),
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

	}

	private void buildGraph(Classes cls) {
		classes = new HashMap<String, Class_>();
		tree = new HashMap<String, String>();

		for (Iterator<SingleNode> e = cls.getElements(); e.hasNext();) {
			Class_ c = (Class_) e.next();
			String name = c.name;

			if (name.equals("Int") || name.equals("String")
					|| name.equals("Bool") || name.equals("Object")
					|| name.equals("IO"))
				abort("Redefinition of basic class " + name + ".", c);
			
			if(name.equals("SELF_TYPE"))
				abort("Redefinition of basic class SELF_TYPE.", c);

			if (classes.containsKey(name))
				abort("Class " + name + " was previously defined.", c);

			classes.put(name, c);

		}

		bassicClasses();

		UnionFind uf = new UnionFind();
		uf.union("Object", "IO");
		uf.union("Object", "Int");
		uf.union("Object", "Bool");
		uf.union("Object", "String");

		for (Iterator<SingleNode> e = cls.getElements(); e.hasNext();) {
			Class_ c = (Class_) e.next();
			String name = c.name;
			String parent = c.parent;

			if (parent.equals("Int") || parent.equals("String")
					|| parent.equals("Bool"))
				abort("Class " + name + " cannot inherit from class " + parent
						+ ".", c);

			if (!classes.containsKey(parent))
				abort("Class " + name + " cannot inherit class " + parent + ".",
						c);

			tree.put(name, parent);
			uf.union(name, parent);
		}

		if (uf.getMaxRank() < 5 + cls.getLength()) {
			System.err.println("Inheritance graph contains cycles");
			System.exit(0);
		}

	}

	public Class_ getClass(String className){
		return classes.get(className);
	}
	
	private void abort(String s, Class_ cl) {
		semantError(cl).println(s);
		System.err.println("Compilation halted due to static semantic errors");
		System.exit(0);
	}

	private void abort(String s) {
		semantError().println(s);
		System.err.println("Compilation halted due to static semantic errors");
		System.exit(0);
	}

	private void checkOverride() {
		for (Class_ cl : classes.values()) {
			if (cl.name.equals("Object"))
				continue;
			Features fts = cl.features;

			for (Iterator<SingleNode> e = fts.getElements(); e.hasNext();) {
				SingleNode f = e.next();
				if (f instanceof attr) {
					attr at = (attr) f;
					if (attrLookup(tree.get(cl.name), at.name) != null)
						abort("Attribute " + at.name
								+ " is an attribute of an inherited class.", cl);

				} else if (f instanceof method) {
					method m = (method) f;

					method m2 = methodLookup(tree.get(cl.name), m.name);

					if (m2 != null && !m.compareSignature(m2))
						abort("Bad method overriding", cl);

				} else
					Utilities
							.fatalError("Unexpected element on class definition");
			}
		}
	}

	public String lca(String class1, String class2) {
		Set<String> path1 = new HashSet<String>();

		while (class1 != null) {
			path1.add(class1);
			class1 = tree.get(class1);
		}

		while (!path1.contains(class2))
			class2 = tree.get(class2);

		return class2;
	}

	public ArrayList<attr> lookupAttributes(String strClass) {
		if (strClass == null)
			return new ArrayList<attr>();

		ArrayList<attr> list = lookupAttributes(tree.get(strClass));

		for (attr a : getAttributes(strClass))
			list.add(a);

		return list;
	}

	public ArrayList<attr> getAttributes(String strClass) {
		ArrayList<attr> attrs = new ArrayList<attr>();

		Class_ cl = classes.get(strClass);
		if (cl == null)
			return attrs;
		for (Iterator<SingleNode> e = cl.features.getElements(); e.hasNext();) {
			SingleNode f = e.next();
			if (f instanceof attr) {
				attr a = (attr) f;
				attrs.add(a);
			} else if (f instanceof method) {
			} else {
				System.err.println("Unexpected error");
				System.exit(0);
			}
		}

		return attrs;
	}

	public ArrayList<method> getMethods(String strClass) {
		ArrayList<method> methods = new ArrayList<method>();

		Class_ cl = classes.get(strClass);
		for (Iterator<SingleNode> e = cl.features.getElements(); e.hasNext();) {
			SingleNode f = e.next();
			if (f instanceof attr) {
			} else if (f instanceof method) {
				method m = (method) f;
				methods.add(m);
			} else {
				System.err.println("Unexpected error");
				System.exit(0);
			}
		}

		return methods;
	}

	public attr getAttribute(String strClass, String attrName) {
		for (attr m : getAttributes(strClass)) {
			if (m.name.equals(attrName))
				return m;
		}
		return null;
	}

	public method getMethod(String strClass, String methodName) {
		for (method m : getMethods(strClass)) {
			if (m.name.equals(methodName))
				return m;
		}
		return null;
	}

	public method methodLookup(String strClass, String methodName) {
		method m = getMethod(strClass, methodName);
		if (m != null)
			return m;

		if (strClass.equals("Object"))
			return null;
		return methodLookup(tree.get(strClass), methodName);
	}

	public attr attrLookup(String strClass, String attrName) {
		for (attr a : getAttributes(strClass)) {
			if (a.name.equals(attrName))
				return a;
		}
		if (strClass.equals("Object"))
			return null;
		return attrLookup(tree.get(strClass), attrName);
	}

	public boolean subType(String T1, String T2) {
		if (T1 == null)
			return false;

		if (T1.equals(T2))
			return true;

		return subType(tree.get(T1), T2);
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
	public PrintStream semantError(String filename, SingleNode t) {
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