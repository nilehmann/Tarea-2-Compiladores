package cool.compiler;
import java.io.PrintStream;
import java.util.ArrayList;



class Classes extends ListNode {

	protected Classes(int lineNumber, ArrayList<SingleNode> elements) {
		super(lineNumber, elements);
	}

	/** Creates an empty "Classes" list */
	public Classes(int lineNumber) {
		super(lineNumber);
	}

	/** Appends "Class_" element to this list */
	public Classes appendElement(SingleNode elem) {
		addElement(elem);
		return this;
	}

	public ListNode copy() {
		return new Classes(lineNumber, copyElements());
	}

	public <R, R2, P1, P2> R2 accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

class Features extends ListNode {

	protected Features(int lineNumber, ArrayList<SingleNode> elements) {
		super(lineNumber, elements);
	}

	/** Creates an empty "Features" list */
	public Features(int lineNumber) {
		super(lineNumber);
	}

	/** Appends "Feature" element to this list */
	public Features appendElement(SingleNode elem) {
		addElement(elem);
		return this;
	}

	public ListNode copy() {
		return new Features(lineNumber, copyElements());
	}

	public <R, R2, P1, P2> R2 accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines list phylum Formals
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Formals extends ListNode {

	protected Formals(int lineNumber, ArrayList<SingleNode> elements) {
		super(lineNumber, elements);
	}

	/** Creates an empty "Formals" list */
	public Formals(int lineNumber) {
		super(lineNumber);
	}

	/** Appends "Formal" element to this list */
	public Formals appendElement(SingleNode elem) {
		addElement(elem);
		return this;
	}

	public ListNode copy() {
		return new Formals(lineNumber, copyElements());
	}

	public <R, R2, P1, P2> R2 accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/** Defines simple phylum Expression */
abstract class Expression extends SingleNode {
	protected Expression(int lineNumber) {
		super(lineNumber);
	}

	private AbstractType type = null;

	public AbstractType get_type() {
		return type;
	}

	public Expression set_type(AbstractType s) {
		type = s;
		return this;
	}


	public void dump_type(PrintStream out, int n) {
		if (type != null) {
			out.println(Utilities.pad(n) + ": " + type.toString());
		} else {
			out.println(Utilities.pad(n) + ": _no_type");
		}
	}

}

/**
 * Defines list phylum Expressions
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Expressions extends ListNode {
	protected Expressions(int lineNumber, ArrayList<SingleNode> elements) {
		super(lineNumber, elements);
	}

	/** Creates an empty "Expressions" list */
	public Expressions(int lineNumber) {
		super(lineNumber);
	}

	/** Appends "Expression" element to this list */
	public Expressions appendElement(SingleNode elem) {
		addElement(elem);
		return this;
	}

	public ListNode copy() {
		return new Expressions(lineNumber, copyElements());
	}

	public <R, R2, P1, P2> R2 accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines list phylum Cases
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Cases extends ListNode {

	protected Cases(int lineNumber, ArrayList<SingleNode>  elements) {
		super(lineNumber, elements);
	}

	/** Creates an empty "Cases" list */
	public Cases(int lineNumber) {
		super(lineNumber);
	}

	/** Appends "Case" element to this list */
	public Cases appendElement(SingleNode elem) {
		addElement(elem);
		return this;
	}

	public ListNode copy() {
		return new Cases(lineNumber, copyElements());
	}

	public <R, R2, P1, P2> R2 accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'programc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class Program extends SingleNode {
	protected Classes classes;

	/**
	 * Creates "programc" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for classes
	 */
	public Program(int lineNumber, Classes a1) {
		super(lineNumber);
		classes = a1;
	}

	public SingleNode copy() {
		return new Program(lineNumber, (Classes) classes.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
	
}

/**
 * Defines AST constructor 'class_c'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class Class_ extends SingleNode {
	protected String name;
	protected String parent;
	protected Features features;
	protected String filename;

	/**
	 * Creates "class_c" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for name
	 * @param a1
	 *            initial value for parent
	 * @param a2
	 *            initial value for features
	 * @param a3
	 *            initial value for filename
	 */
	public Class_(int lineNumber, String a1, String a2, Features a3, String a4) {
		super(lineNumber);
		name = a1;
		parent = a2;
		features = a3;
		filename = a4;
	}

	public SingleNode copy() {
		return new Class_(lineNumber, copy_String(name), copy_String(parent),
				(Features) features.copy(), copy_String(filename));
	}


	public String getFilename() {
		return filename;
	}

	public String getName() {
		return name;
	}

	public String getParent() {
		return parent;
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}


abstract class Feature extends SingleNode {
	protected Feature(int lineNumber) {
		super(lineNumber);
	}
}

/**
 * Defines AST constructor 'method'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class method extends Feature {
	protected String name;
	protected Formals formals;
	protected String return_type;
	protected Expression expr;

	/**
	 * Creates "method" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for name
	 * @param a1
	 *            initial value for formals
	 * @param a2
	 *            initial value for return_type
	 * @param a3
	 *            initial value for expr
	 */
	public method(int lineNumber, String a1, Formals a2, String a3,
			Expression a4) {
		super(lineNumber);
		name = a1;
		formals = a2;
		return_type = a3;
		expr = a4;
	}

	public SingleNode copy() {
		return new method(lineNumber, copy_String(name),
				(Formals) formals.copy(), copy_String(return_type),
				(Expression) expr.copy());
	}
	
	public boolean compareSignature(method m2){
		if(!return_type.equals(m2.return_type))
			return false;
		if(formals.getLength() != m2.formals.getLength())
			return false;
		int N = formals.getLength();
		
		for (int i = 0; i < N; i++) {
			Formal f1 = (Formal) formals.getNth(i);
			Formal f2 = (Formal) m2.formals.getNth(i);
			if(!f1.type_decl.equals(f2.type_decl))
				return false;
		}
		return true;
		
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class attr extends Feature {
	protected String name;
	protected String type_decl;
	protected Expression init;

	/**
	 * Creates "attr" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for name
	 * @param a1
	 *            initial value for type_decl
	 * @param a2
	 *            initial value for init
	 */
	public attr(int lineNumber, String a1, String a2, Expression a3) {
		super(lineNumber);
		name = a1;
		type_decl = a2;
		init = a3;
	}

	public SingleNode copy() {
		return new attr(lineNumber, copy_String(name), copy_String(type_decl),
				(Expression) init.copy());
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}

}

/**
 * Defines AST constructor 'formalc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class Formal extends SingleNode {
	protected String name;
	protected String type_decl;

	/**
	 * Creates "formalc" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for name
	 * @param a1
	 *            initial value for type_decl
	 */
	public Formal(int lineNumber, String a1, String a2) {
		super(lineNumber);
		name = a1;
		type_decl = a2;
	}

	public SingleNode copy() {
		return new Formal(lineNumber, copy_String(name), copy_String(type_decl));
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'branch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class Branch extends SingleNode {
	protected String name;
	protected String type_decl;
	protected Expression expr;

	/**
	 * Creates "branch" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for name
	 * @param a1
	 *            initial value for type_decl
	 * @param a2
	 *            initial value for expr
	 */
	public Branch(int lineNumber, String a1, String a2, Expression a3) {
		super(lineNumber);
		name = a1;
		type_decl = a2;
		expr = a3;
	}

	public SingleNode copy() {
		return new Branch(lineNumber, copy_String(name),
				copy_String(type_decl), (Expression) expr.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}

}

/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class assign extends Expression {
	protected String name;
	protected Expression expr;

	/**
	 * Creates "assign" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for name
	 * @param a1
	 *            initial value for expr
	 */
	public assign(int lineNumber, String a1, Expression a2) {
		super(lineNumber);
		name = a1;
		expr = a2;
	}

	public SingleNode copy() {
		return new assign(lineNumber, copy_String(name),
				(Expression) expr.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'static_dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class static_dispatch extends Expression {
	protected Expression expr;
	protected String type_name;
	protected String name;
	protected Expressions actual;

	/**
	 * Creates "static_dispatch" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for expr
	 * @param a1
	 *            initial value for type_name
	 * @param a2
	 *            initial value for name
	 * @param a3
	 *            initial value for actual
	 */
	public static_dispatch(int lineNumber, Expression a1, String a2, String a3,
			Expressions a4) {
		super(lineNumber);
		expr = a1;
		type_name = a2;
		name = a3;
		actual = a4;
	}

	public SingleNode copy() {
		return new static_dispatch(lineNumber, (Expression) expr.copy(),
				copy_String(type_name), copy_String(name),
				(Expressions) actual.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class dispatch extends Expression {
	protected Expression expr;
	protected String name;
	protected Expressions actual;

	/**
	 * Creates "dispatch" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for expr
	 * @param a1
	 *            initial value for name
	 * @param a2
	 *            initial value for actual
	 */
	public dispatch(int lineNumber, Expression a1, String a2, Expressions a3) {
		super(lineNumber);
		expr = a1;
		name = a2;
		actual = a3;
	}

	public SingleNode copy() {
		return new dispatch(lineNumber, (Expression) expr.copy(),
				copy_String(name), (Expressions) actual.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}

}

/**
 * Defines AST constructor 'cond'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class cond extends Expression {
	protected Expression pred;
	protected Expression then_exp;
	protected Expression else_exp;

	/**
	 * Creates "cond" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for pred
	 * @param a1
	 *            initial value for then_exp
	 * @param a2
	 *            initial value for else_exp
	 */
	public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
		super(lineNumber);
		pred = a1;
		then_exp = a2;
		else_exp = a3;
	}

	public SingleNode copy() {
		return new cond(lineNumber, (Expression) pred.copy(),
				(Expression) then_exp.copy(), (Expression) else_exp.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'loop'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class loop extends Expression {
	protected Expression pred;
	protected Expression body;

	/**
	 * Creates "loop" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for pred
	 * @param a1
	 *            initial value for body
	 */
	public loop(int lineNumber, Expression a1, Expression a2) {
		super(lineNumber);
		pred = a1;
		body = a2;
	}

	public SingleNode copy() {
		return new loop(lineNumber, (Expression) pred.copy(),
				(Expression) body.copy());
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}

}

/**
 * Defines AST constructor 'typcase'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class typecase extends Expression {
	protected Expression expr;
	protected Cases cases;

	/**
	 * Creates "typcase" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for expr
	 * @param a1
	 *            initial value for cases
	 */
	public typecase(int lineNumber, Expression a1, Cases a2) {
		super(lineNumber);
		expr = a1;
		cases = a2;
	}

	public SingleNode copy() {
		return new typecase(lineNumber, (Expression) expr.copy(),
				(Cases) cases.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'block'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class block extends Expression {
	protected Expressions body;

	/**
	 * Creates "block" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for body
	 */
	public block(int lineNumber, Expressions a1) {
		super(lineNumber);
		body = a1;
	}

	public SingleNode copy() {
		return new block(lineNumber, (Expressions) body.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'let'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class let extends Expression {
	protected String identifier;
	protected String type_decl;
	protected Expression init;
	protected Expression body;

	/**
	 * Creates "let" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for identifier
	 * @param a1
	 *            initial value for type_decl
	 * @param a2
	 *            initial value for init
	 * @param a3
	 *            initial value for body
	 */
	public let(int lineNumber, String a1, String a2, Expression a3,
			Expression a4) {
		super(lineNumber);
		identifier = a1;
		type_decl = a2;
		init = a3;
		body = a4;
	}

	public SingleNode copy() {
		return new let(lineNumber, copy_String(identifier),
				copy_String(type_decl), (Expression) init.copy(),
				(Expression) body.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'plus'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class plus extends Expression {
	protected Expression e1;
	protected Expression e2;

	/**
	 * Creates "plus" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 * @param a1
	 *            initial value for e2
	 */
	public plus(int lineNumber, Expression a1, Expression a2) {
		super(lineNumber);
		e1 = a1;
		e2 = a2;
	}

	public SingleNode copy() {
		return new plus(lineNumber, (Expression) e1.copy(),
				(Expression) e2.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'sub'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class sub extends Expression {
	protected Expression e1;
	protected Expression e2;

	/**
	 * Creates "sub" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 * @param a1
	 *            initial value for e2
	 */
	public sub(int lineNumber, Expression a1, Expression a2) {
		super(lineNumber);
		e1 = a1;
		e2 = a2;
	}

	public SingleNode copy() {
		return new sub(lineNumber, (Expression) e1.copy(),
				(Expression) e2.copy());
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'mul'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class mul extends Expression {
	protected Expression e1;
	protected Expression e2;

	/**
	 * Creates "mul" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 * @param a1
	 *            initial value for e2
	 */
	public mul(int lineNumber, Expression a1, Expression a2) {
		super(lineNumber);
		e1 = a1;
		e2 = a2;
	}

	public SingleNode copy() {
		return new mul(lineNumber, (Expression) e1.copy(),
				(Expression) e2.copy());
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'divide'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class divide extends Expression {
	protected Expression e1;
	protected Expression e2;

	/**
	 * Creates "divide" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 * @param a1
	 *            initial value for e2
	 */
	public divide(int lineNumber, Expression a1, Expression a2) {
		super(lineNumber);
		e1 = a1;
		e2 = a2;
	}

	public SingleNode copy() {
		return new divide(lineNumber, (Expression) e1.copy(),
				(Expression) e2.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'neg'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class neg extends Expression {
	protected Expression e1;

	/**
	 * Creates "neg" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 */
	public neg(int lineNumber, Expression a1) {
		super(lineNumber);
		e1 = a1;
	}

	public SingleNode copy() {
		return new neg(lineNumber, (Expression) e1.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'lt'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class lt extends Expression {
	protected Expression e1;
	protected Expression e2;

	/**
	 * Creates "lt" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 * @param a1
	 *            initial value for e2
	 */
	public lt(int lineNumber, Expression a1, Expression a2) {
		super(lineNumber);
		e1 = a1;
		e2 = a2;
	}

	public SingleNode copy() {
		return new lt(lineNumber, (Expression) e1.copy(),
				(Expression) e2.copy());
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'eq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class eq extends Expression {
	protected Expression e1;
	protected Expression e2;

	/**
	 * Creates "eq" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 * @param a1
	 *            initial value for e2
	 */
	public eq(int lineNumber, Expression a1, Expression a2) {
		super(lineNumber);
		e1 = a1;
		e2 = a2;
	}

	public SingleNode copy() {
		return new eq(lineNumber, (Expression) e1.copy(),
				(Expression) e2.copy());
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'leq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class leq extends Expression {
	protected Expression e1;
	protected Expression e2;

	/**
	 * Creates "leq" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 * @param a1
	 *            initial value for e2
	 */
	public leq(int lineNumber, Expression a1, Expression a2) {
		super(lineNumber);
		e1 = a1;
		e2 = a2;
	}

	public SingleNode copy() {
		return new leq(lineNumber, (Expression) e1.copy(),
				(Expression) e2.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'comp'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class comp extends Expression {
	protected Expression e1;

	/**
	 * Creates "comp" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 */
	public comp(int lineNumber, Expression a1) {
		super(lineNumber);
		e1 = a1;
	}

	public SingleNode copy() {
		return new comp(lineNumber, (Expression) e1.copy());
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'int_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class int_const extends Expression {
	protected String token;

	/**
	 * Creates "int_const" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for token
	 */
	public int_const(int lineNumber, String a1) {
		super(lineNumber);
		token = a1;
	}

	public SingleNode copy() {
		return new int_const(lineNumber, copy_String(token));
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}

}

/**
 * Defines AST constructor 'bool_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class bool_const extends Expression {
	protected Boolean val;

	/**
	 * Creates "bool_const" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for val
	 */
	public bool_const(int lineNumber, Boolean a1) {
		super(lineNumber);
		val = a1;
	}

	public SingleNode copy() {
		return new bool_const(lineNumber, copy_Boolean(val));
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'string_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class string_const extends Expression {
	protected String token;

	/**
	 * Creates "string_const" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for token
	 */
	public string_const(int lineNumber, String a1) {
		super(lineNumber);
		token = a1;
	}

	public SingleNode copy() {
		return new string_const(lineNumber, copy_String(token));
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class new_ extends Expression {
	protected String type_name;

	/**
	 * Creates "new_" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for type_name
	 */
	public new_(int lineNumber, String a1) {
		super(lineNumber);
		type_name = a1;
	}

	public SingleNode copy() {
		return new new_(lineNumber, copy_String(type_name));
	}


	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'isvoid'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class isvoid extends Expression {
	protected Expression e1;

	/**
	 * Creates "isvoid" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for e1
	 */
	public isvoid(int lineNumber, Expression a1) {
		super(lineNumber);
		e1 = a1;
	}

	public SingleNode copy() {
		return new isvoid(lineNumber, (Expression) e1.copy());
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'no_expr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class no_expr extends Expression {
	/**
	 * Creates "no_expr" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 */
	public no_expr(int lineNumber) {
		super(lineNumber);
	}

	public SingleNode copy() {
		return new no_expr(lineNumber);
	}

	public void dump(PrintStream out, int n) {
		out.print(Utilities.pad(n) + "no_expr\n");
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class object extends Expression {
	protected String name;

	/**
	 * Creates "object" AST node.
	 * 
	 * @param lineNumber
	 *            the line in the source file from which this node came.
	 * @param a0
	 *            initial value for name
	 */
	public object(int lineNumber, String a1) {
		super(lineNumber);
		name = a1;
	}

	public SingleNode copy() {
		return new object(lineNumber, copy_String(name));
	}

	public <R, R2, P1, P2> R accept(ASTVisitor<R, R2, P1, P2> visitor, P1 arg1, P2 arg2) {
		return visitor.visit(this, arg1, arg2);
	}
}
