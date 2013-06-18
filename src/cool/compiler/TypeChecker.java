package cool.compiler;

import java.util.Iterator;

public class TypeChecker
		implements
		ASTVisitor<AbstractType, ListType, SymbolTable<String, AbstractType>, Class_> {
	ClassTable classTable;
	static final AbstractType object_type = new Type("Object");
	static final AbstractType int_type = new Type("Int");
	static final AbstractType string_type = new Type("String");
	static final AbstractType bool_type = new Type("Bool");

	
	public TypeChecker(ClassTable ct) {
		classTable = ct;
	}

	@Override
	public ListType visit(Classes classes, SymbolTable<String, AbstractType> O,
			Class_ C) {

		for (Iterator<SingleNode> e = classes.getElements(); e.hasNext();)
			e.next().accept(this, O, C);
		return null;
	}

	@Override
	public ListType visit(Features features,
			SymbolTable<String, AbstractType> O, Class_ C) {
		for (Iterator<SingleNode> e = features.getElements(); e.hasNext();)
			e.next().accept(this, O, C);
		return null;
	}

	@Override
	public ListType visit(Formals formals, SymbolTable<String, AbstractType> O,
			Class_ C) {
		ListType t = new ListType();
		for (Iterator<SingleNode> e = formals.getElements(); e.hasNext();)
			t.addType(e.next().accept(this, O, C));

		return t;
	}

	@Override
	public ListType visit(Expressions exprs,
			SymbolTable<String, AbstractType> O, Class_ C) {
		ListType t = new ListType();
		for (Iterator<SingleNode> e = exprs.getElements(); e.hasNext();)
			t.addType(e.next().accept(this, O, C));

		return t;
	}

	@Override
	public ListType visit(Cases cases, SymbolTable<String, AbstractType> O,
			Class_ C) {
		ListType t = new ListType();
		for (int i = 0; i < cases.getLength(); ++i) {
			Branch b1 = (Branch) cases.getNth(i);
			for (int j = i + 1; j < cases.getLength(); j++) {
				Branch b2 = (Branch) cases.getNth(j);
				if (b1.type_decl.equals(b2.type_decl))
					classTable.semantError(C).println(
							"Duplicate branch " + b1.type_decl
									+ " in case statement.");

			}

			t.addType(b1.accept(this, O, C));
		}

		return t;
	}

	@Override
	public AbstractType visit(Program program,
			SymbolTable<String, AbstractType> O, Class_ C) {
		program.classes.accept(this, O, C);
		return null;
	}

	@Override
	public AbstractType visit(Class_ cl, SymbolTable<String, AbstractType> O,
			Class_ C) {
		O = new SymbolTable<String, AbstractType>();
		O.enterScope();
		for (attr a : classTable.lookupAttributes(cl.name)) {
			if (a.name.equals("self"))
				classTable.semantError(C).println(
						"'self' cannot be the name of a attribute.");
			else {
				AbstractType t = resolveType(a.type_decl, C, null);
				O.addId(a.name, t);
			}
		}
		O.addId("self", new SelfType(cl.name));
		cl.features.accept(this, O, cl);
		O.exitScope();

		return null;
	}

	@Override
	public AbstractType visit(method m, SymbolTable<String, AbstractType> O,
			Class_ C) {
		O.enterScope();

		for (Iterator<SingleNode> it = m.formals.getElements(); it.hasNext();) {
			Formal f = (Formal) it.next();
			if (f.type_decl.equals("SELF_TYPE"))
				classTable.semantError(C).println(
						"Parameter cannot be of type SELF_TYPE.");
			else if (f.name.equals("self"))
				classTable.semantError(C).println(
						"'self' cannot be the name of a parameter.");
			else if (O.probe(f.name) != null)
				classTable.semantError(C).println(
						"Duplicate parameter " + f.name);
			else
				O.addId(f.name, new Type(f.type_decl));
		}

		AbstractType T0 = resolveType(m.return_type, C,
				"Undefined return type " + m.return_type + " in method "
						+ m.name + ".");

		AbstractType T2 = m.expr.accept(this, O, C);

		if (!T2.subType(T0, classTable))
			classTable.semantError(C).println("Type mismatch");

		O.exitScope();

		return null;
	}

	@Override
	public AbstractType visit(attr at, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = at.init.accept(this, O, C);
		if (!T1.subType(new Type(at.type_decl), classTable))
			classTable.semantError(C).println("Type mismatch");

		return null;
	}

	@Override
	public AbstractType visit(Formal formal,
			SymbolTable<String, AbstractType> O, Class_ C) {
		return new Type(formal.type_decl);
	}

	@Override
	public AbstractType visit(Branch branch,
			SymbolTable<String, AbstractType> O, Class_ C) {

		O.enterScope();
		O.addId(branch.name, new Type(branch.type_decl));
		AbstractType T = branch.expr.accept(this, O, C);
		O.exitScope();

		return T;
	}

	@Override
	public AbstractType visit(assign a, SymbolTable<String, AbstractType> O,
			Class_ C) {

		if (a.name.equals("self"))
			classTable.semantError(C).println("Cannot assign to 'self'.");

		AbstractType T = O.lookup(a.name);
		if (T == null) {
			classTable.semantError(C).println(a.name + " cannot be resolved");
			a.set_type(object_type);
			return object_type;
		}

		AbstractType T2 = a.expr.accept(this, O, C);

		if (!T2.subType(T, classTable)) {
			classTable.semantError(C).println("type mistmatch " + a.name);
			a.set_type(object_type);
			return object_type;
		}

		a.set_type(T2);
		return T2;
	}

	@Override
	public AbstractType visit(static_dispatch sd,
			SymbolTable<String, AbstractType> O, Class_ C) {
		AbstractType T0 = sd.expr.accept(this, O, C);

		if (!T0.subType(new Type(sd.type_name), classTable))
			classTable
					.semantError(C)
					.println(
							"Expression type "
									+ T0.toString()
									+ " does not conform to declared static dispatch type "
									+ sd.type_name + ".");

		method m = classTable.methodLookup(sd.type_name, sd.name);
		if (m == null) {
			classTable.semantError(C).println(
					"Dispatch to undefined method " + sd.name + ".");
			sd.set_type(object_type);
			return object_type;
		}

		ListType Tn = sd.actual.accept(this, O, C);
		ListType Tnp = m.formals.accept(this, O, C);
		if (!Tn.subType(Tnp, classTable)) {
			classTable.semantError(C).println(
					"Cannot call method " + sd.name
							+ " with the given parameters.");
			sd.set_type(object_type);
			return object_type;
		}

		AbstractType Tn1;
		if (m.return_type == "SELF_TYPE")
			Tn1 = T0;
		else
			Tn1 = new Type(m.return_type);

		sd.set_type(Tn1);

		return Tn1;
	}

	@Override
	public AbstractType visit(dispatch d, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T0 = d.expr.accept(this, O, C);

		method m = classTable.methodLookup(T0.getClassForLookup(), d.name);
		if (m == null) {
			classTable.semantError(C).println(
					"Dispatch to undefined method " + d.name + ".");
			d.set_type(object_type);
			return object_type;
		}

		ListType Tn = d.actual.accept(this, O, C);
		ListType Tnp = m.formals.accept(this, O, C);

		if (!Tn.subType(Tnp, classTable)) {
			classTable.semantError(C).println(
					"Cannot call method " + d.name
							+ " with the given parameters.");
			d.set_type(object_type);
			return object_type;
		}

		AbstractType Tn1;
		if (m.return_type.equals("SELF_TYPE"))
			Tn1 = T0;
		else
			Tn1 = new Type(m.return_type);

		d.set_type(Tn1);

		return Tn1;

	}

	@Override
	public AbstractType visit(cond c, SymbolTable<String, AbstractType> O,
			Class_ C) {

		AbstractType T1 = c.pred.accept(this, O, C);
		if (!T1.equals("Bool")) {
			classTable.semantError(C).println(
					"If predicate must be a boolean value");
			c.set_type(object_type);
			return object_type;
		}

		AbstractType T2 = c.then_exp.accept(this, O, C);
		AbstractType T3 = c.else_exp.accept(this, O, C);

		AbstractType T = T2.lca(T3, classTable);

		c.set_type(T);

		return T;
	}

	@Override
	public AbstractType visit(loop l, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = l.pred.accept(this, O, C);
		if (!T1.equals("Bool")) {
			classTable.semantError(C).println(
					"Loop predicate must be a boolean value");
			l.set_type(object_type);
			return object_type;
		}
		l.body.accept(this, O, C);

		l.set_type(object_type);
		return object_type;
	}

	@Override
	public AbstractType visit(typecase t, SymbolTable<String, AbstractType> O,
			Class_ C) {
		t.expr.accept(this, O, C);
		ListType l = t.cases.accept(this, O, C);

		AbstractType T = l.lca(classTable);

		t.set_type(T);

		return T;
	}

	@Override
	public AbstractType visit(block b, SymbolTable<String, AbstractType> O,
			Class_ C) {
		ListType t = b.body.accept(this, O, C);
		AbstractType T = t.last();
		b.set_type(T);

		return T;
	}

	private AbstractType resolveType(String type, Class_ C, String errorMessage) {
		if (type.equals("SELF_TYPE"))
			return new SelfType(C.name);
		else {
			if (classTable.getClass(type) == null) {
				if (errorMessage != null)
					classTable.semantError(C).println(errorMessage);
				return object_type;
			} else
				return new Type(type);
		}
	}

	@Override
	public AbstractType visit(let l, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T0 = resolveType(l.type_decl, C, "Undefined type "
				+ l.type_decl + " in let expression.");

		AbstractType T1 = l.init.accept(this, O, C);

		if (l.identifier.equals("self"))
			classTable.semantError(C).println(
					"'self' cannot be bound in a 'let' expression.");

		if (!T1.subType(T0, classTable))
			classTable.semantError(C).println("Type mismatch");

		O.enterScope();

		O.addId(l.identifier, T0);
		AbstractType T2 = l.body.accept(this, O, C);

		O.exitScope();

		l.set_type(T2);

		return T2;
	}

	@Override
	public AbstractType visit(plus p, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = p.e1.accept(this, O, C);
		AbstractType T2 = p.e2.accept(this, O, C);

		if (!T1.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		if (!T2.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		p.set_type(int_type);
		return int_type;
	}

	@Override
	public AbstractType visit(sub s, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = s.e1.accept(this, O, C);
		AbstractType T2 = s.e2.accept(this, O, C);

		if (!T1.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		if (!T2.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		s.set_type(int_type);
		return int_type;
	}

	@Override
	public AbstractType visit(mul m, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = m.e1.accept(this, O, C);
		AbstractType T2 = m.e2.accept(this, O, C);

		if (!T1.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		if (!T2.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		m.set_type(int_type);
		return int_type;
	}

	@Override
	public AbstractType visit(divide d, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = d.e1.accept(this, O, C);
		AbstractType T2 = d.e2.accept(this, O, C);

		if (!T1.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		if (!T2.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		d.set_type(int_type);
		return int_type;
	}

	@Override
	public AbstractType visit(neg n, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T = n.e1.accept(this, O, C);

		if (!T.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		n.set_type(int_type);
		return int_type;
	}

	@Override
	public AbstractType visit(lt l, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = l.e1.accept(this, O, C);
		AbstractType T2 = l.e2.accept(this, O, C);

		if (!T1.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		if (!T2.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		l.set_type(bool_type);
		return bool_type;
	}

	@Override
	public AbstractType visit(leq l, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = l.e1.accept(this, O, C);
		AbstractType T2 = l.e2.accept(this, O, C);

		if (!T1.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		if (!T2.equals("Int"))
			classTable.semantError(C).println("Type mismatch");

		l.set_type(bool_type);
		return bool_type;
	}

	@Override
	public AbstractType visit(eq e, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T1 = e.e1.accept(this, O, C);
		AbstractType T2 = e.e2.accept(this, O, C);

		if (T1.equals("Int") || T1.equals("Bool") || T1.equals("String")
				|| T2.equals("Int") || T2.equals("Bool") || T2.equals("String")) {
			if (!T1.equals(T2))
				classTable.semantError(C).println("Type mismatch");
		}

		e.set_type(bool_type);
		return bool_type;
	}

	@Override
	public AbstractType visit(comp c, SymbolTable<String, AbstractType> O,
			Class_ C) {
		c.e1.accept(this, O, C);

		c.set_type(bool_type);
		return bool_type;
	}

	@Override
	public AbstractType visit(int_const ic,
			SymbolTable<String, AbstractType> O, Class_ C) {

		ic.set_type(int_type);
		return int_type;
	}

	@Override
	public AbstractType visit(bool_const bc,
			SymbolTable<String, AbstractType> O, Class_ C) {

		bc.set_type(bool_type);
		return bool_type;
	}

	@Override
	public AbstractType visit(string_const sc,
			SymbolTable<String, AbstractType> O, Class_ C) {

		sc.set_type(string_type);
		return string_type;
	}

	@Override
	public AbstractType visit(new_ n, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T = resolveType(n.type_name, C,
				"'new' used with undefined class " + n.type_name + ".");

		n.set_type(T);
		return T;
	}

	@Override
	public AbstractType visit(isvoid iv, SymbolTable<String, AbstractType> O,
			Class_ C) {
		iv.e1.accept(this, O, C);

		iv.set_type(bool_type);
		return bool_type;
	}

	@Override
	public AbstractType visit(no_expr ne, SymbolTable<String, AbstractType> O,
			Class_ C) {
		return new NoType();
	}

	@Override
	public AbstractType visit(object o, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType type = O.lookup(o.name);
		if (type != null) {
			o.set_type(type);
			return type;
		}
		classTable.semantError(C).println(o.name + " cannot be resolved");

		o.set_type(object_type);
		return object_type;
	}
}