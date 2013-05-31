import java.io.PrintStream;
import java.util.Enumeration;


public class DumpVisitor implements ASTVisitor<Void, PrintStream, Integer> {

	@Override
	public Void visit(Classes classes, PrintStream out, Integer n) {
		for (Enumeration<TreeNode> e = classes.getElements(); e.hasMoreElements();) {
			e.nextElement().accept(this, out, n);
		}
		return null;
	}

	@Override
	public Void visit(Features features, PrintStream out, Integer n) {
		out.println("\"\n" + Utilities.pad(n + 2) + "(");		
		for (Enumeration<TreeNode> e = features.getElements(); e.hasMoreElements();) {
			e.nextElement().accept(this, out, n);
		}
		out.println(Utilities.pad(n + 2) + ")");
		return null;
	}

	@Override
	public Void visit(Formals formals, PrintStream out, Integer n) {
		for (Enumeration<TreeNode> e = formals.getElements(); e.hasMoreElements();) {
			e.nextElement().accept(this, out, n);
		}
		return null;
	}

	@Override
	public Void visit(Expressions exprs, PrintStream out, Integer n) {
		for (Enumeration<TreeNode> e = exprs.getElements(); e.hasMoreElements();) {
			e.nextElement().accept(this, out, n);
		}
		return null;
	}

	@Override
	public Void visit(Cases cases, PrintStream out, Integer n) {
		for (Enumeration<TreeNode> e = cases.getElements(); e.hasMoreElements();) {
			e.nextElement().accept(this, out, n);
		}
		return null;
	}

	@Override
	public Void visit(Program program, PrintStream out, Integer n) {
		program.dump_line(out, n);
		out.println(Utilities.pad(n) + "_program");
		program.classes.accept(this, out, n+2);
		return null;
	}

	@Override
	public Void visit(Class_ class_, PrintStream out, Integer n) {
		class_.dump_line(out, n);
		out.println(Utilities.pad(n) + "_class");
		dump_String(out, n + 2, class_.name);
		dump_String(out, n + 2, class_.parent);
		out.print(Utilities.pad(n + 2) + "\"");
		Utilities.printEscapedString(out, class_.filename);
		class_.features.accept(this, out, n+2);
		return null;
	}

	@Override
	public Void visit(method m, PrintStream out, Integer n) {
		m.dump_line(out, n);
		out.println(Utilities.pad(n) + "_method");
		dump_String(out, n + 2, m.name);
		m.formals.accept(this, out, n+2);
		dump_String(out, n + 2, m.return_type);
		m.expr.accept(this, out, n + 2);
		return null;
	}

	@Override
	public Void visit(attr at, PrintStream out, Integer n) {
		at.dump_line(out, n);
		out.println(Utilities.pad(n) + "_attr");
		dump_String(out, n + 2, at.name);
		dump_String(out, n + 2, at.type_decl);
		at.init.accept(this, out, n + 2);
		return null;
	}

	@Override
	public Void visit(Formal formal, PrintStream out, Integer n) {
		formal.dump_line(out, n);
		out.println(Utilities.pad(n) + "_formal");
		dump_String(out, n + 2, formal.name);
		dump_String(out, n + 2, formal.type_decl);
		return null;
	}

	@Override
	public Void visit(Branch branch, PrintStream out, Integer n) {
		branch.dump_line(out, n);
		out.println(Utilities.pad(n) + "_branch");
		dump_String(out, n + 2, branch.name);
		dump_String(out, n + 2, branch.type_decl);
		branch.expr.accept(this,out, n + 2);
		return null;
	}

	@Override
	public Void visit(assign a, PrintStream out, Integer n) {
		a.dump_line(out, n);
		out.println(Utilities.pad(n) + "_assign");
		dump_String(out, n + 2, a.name);
		a.expr.accept(this,out, n + 2);
		a.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(static_dispatch sd, PrintStream out, Integer n) {
		sd.dump_line(out, n);
		out.println(Utilities.pad(n) + "_static_dispatch");
		sd.expr.accept(this,out, n + 2);
		dump_String(out, n + 2, sd.type_name);
		dump_String(out, n + 2, sd.name);
		out.println(Utilities.pad(n + 2) + "(");
		sd.actual.accept(this, out, n + 2);
		out.println(Utilities.pad(n + 2) + ")");
		sd.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(dispatch d, PrintStream out, Integer n) {
		d.dump_line(out, n);
		out.println(Utilities.pad(n) + "_dispatch");
		d.expr.accept(this,out, n + 2);
		dump_String(out, n + 2, d.name);
		out.println(Utilities.pad(n + 2) + "(");
		d.actual.accept(this, out, n+2);
		out.println(Utilities.pad(n + 2) + ")");
		d.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(cond c, PrintStream out, Integer n) {
		c.dump_line(out, n);
		out.println(Utilities.pad(n) + "_cond");
		c.pred.accept(this,out, n + 2);
		c.then_exp.accept(this,out, n + 2);
		c.else_exp.accept(this,out, n + 2);
		c.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(loop l, PrintStream out, Integer n) {
		l.dump_line(out, n);
		out.println(Utilities.pad(n) + "_loop");
		l.pred.accept(this,out, n + 2);
		l.body.accept(this,out, n + 2);
		l.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(typecase t, PrintStream out, Integer n) {
		t.dump_line(out, n);
		out.println(Utilities.pad(n) + "_typcase");
		t.expr.accept(this,out, n + 2);
		t.cases.accept(this, out, n+2);
		t.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(block b, PrintStream out, Integer n) {
		b.dump_line(out, n);
		out.println(Utilities.pad(n) + "_block");
		b.body.accept(this, out, n+2);
		b.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(let l, PrintStream out, Integer n) {
		l.dump_line(out, n);
		out.println(Utilities.pad(n) + "_let");
		dump_String(out, n + 2, l.identifier);
		dump_String(out, n + 2, l.type_decl);
		l.init.accept(this,out, n + 2);
		l.body.accept(this,out, n + 2);
		l.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(plus p, PrintStream out, Integer n) {
		p.dump_line(out, n);
		out.println(Utilities.pad(n) + "_plus");
		p.e1.accept(this,out, n + 2);
		p.e2.accept(this,out, n + 2);
		p.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(sub s, PrintStream out, Integer n) {
		s.dump_line(out, n);
		out.println(Utilities.pad(n) + "_sub");
		s.e1.accept(this,out, n + 2);
		s.e2.accept(this,out, n + 2);
		s.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(mul m, PrintStream out, Integer n) {
		m.dump_line(out, n);
		out.println(Utilities.pad(n) + "_mul");
		m.e1.accept(this,out, n + 2);
		m.e2.accept(this,out, n + 2);
		m.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(divide d, PrintStream out, Integer n) {
		d.dump_line(out, n);
		out.println(Utilities.pad(n) + "_divide");
		d.e1.accept(this,out, n + 2);
		d.e2.accept(this,out, n + 2);
		d.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(neg ne, PrintStream out, Integer n) {
		ne.dump_line(out, n);
		out.println(Utilities.pad(n) + "_neg");
		ne.e1.accept(this,out, n + 2);
		ne.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(lt l, PrintStream out, Integer n) {
		l.dump_line(out, n);
		out.println(Utilities.pad(n) + "_lt");
		l.e1.accept(this,out, n + 2);
		l.e2.accept(this,out, n + 2);
		l.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(eq e, PrintStream out, Integer n) {
		e.dump_line(out, n);
		out.println(Utilities.pad(n) + "_eq");
		e.e1.accept(this,out, n + 2);
		e.e2.accept(this,out, n + 2);
		e.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(leq l, PrintStream out, Integer n) {
		l.dump_line(out, n);
		out.println(Utilities.pad(n) + "_leq");
		l.e1.accept(this,out, n + 2);
		l.e2.accept(this,out, n + 2);
		l.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(comp c, PrintStream out, Integer n) {
		c.dump_line(out, n);
		out.println(Utilities.pad(n) + "_comp");
		c.e1.accept(this,out, n + 2);
		c.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(int_const ic, PrintStream out, Integer n) {
		ic.dump_line(out, n);
		out.println(Utilities.pad(n) + "_int");
		dump_String(out, n + 2, ic.token);
		ic.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(bool_const bc, PrintStream out, Integer n) {
		bc.dump_line(out, n);
		out.println(Utilities.pad(n) + "_bool");
		dump_Boolean(out, n + 2, bc.val);
		bc.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(string_const sc, PrintStream out, Integer n) {
		sc.dump_line(out, n);
		out.println(Utilities.pad(n) + "_string");
		out.print(Utilities.pad(n + 2) + "\"");
		Utilities.printEscapedString(out, sc.token);
		out.println("\"");
		sc.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(new_ ne, PrintStream out, Integer n) {
		ne.dump_line(out, n);
		out.println(Utilities.pad(n) + "_new");
		dump_String(out, n + 2, ne.type_name);
		ne.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(isvoid iv, PrintStream out, Integer n) {
		iv.dump_line(out, n);
		out.println(Utilities.pad(n) + "_isvoid");
		iv.e1.accept(this,out, n + 2);
		iv.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(no_expr ne, PrintStream out, Integer n) {
		ne.dump_line(out, n);
		out.println(Utilities.pad(n) + "_no_expr");
		ne.dump_type(out, n);
		return null;
	}

	@Override
	public Void visit(object o, PrintStream out, Integer n) {
		o.dump_line(out, n);
		out.println(Utilities.pad(n) + "_object");
		dump_String(out, n + 2, o.name);
		o.dump_type(out, n);
		return null;
	}
	
	/**
	 * Dumps a printable representation of a boolean value.
	 * 
	 * This method is used internally by the generated AST classes
	 * */
	private void dump_Boolean(PrintStream out, int n, Boolean b) {
		out.print(Utilities.pad(n));
		out.println(b.booleanValue() ? "1" : "0");
	}

	/**
	 * Dumps a printable representation of an AbstactSymbol value.
	 * 
	 * This method is used internally by the generated AST classes
	 * */
	private void dump_String(PrintStream out, int n, String sym) {
		out.print(Utilities.pad(n));
		out.println(sym);
	}
}
