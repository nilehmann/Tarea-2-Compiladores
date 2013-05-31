import java.util.Enumeration;

public class TypeChecker implements
		ASTVisitor<AbstractType, SymbolTable<String, AbstractType>, Class_> {
	ClassTable classTable;

	public TypeChecker(ClassTable ct) {
		classTable = ct;
	}

	@Override
	public AbstractType visit(Classes classes,
			SymbolTable<String, AbstractType> O, Class_ C) {
		for (Enumeration<TreeNode> e = classes.getElements(); e
				.hasMoreElements();)
			e.nextElement().accept(this, O, C);
		return null;
	}

	@Override
	public AbstractType visit(Features features,
			SymbolTable<String, AbstractType> O, Class_ C) {
		for (Enumeration<TreeNode> e = features.getElements(); e
				.hasMoreElements();)
			e.nextElement().accept(this, O, C);
		return null;
	}

	@Override
	public AbstractType visit(Formals formals,
			SymbolTable<String, AbstractType> O, Class_ C) {
		ListType t = new ListType();
		for (Enumeration<TreeNode> e = formals.getElements(); e
				.hasMoreElements();)
			t.addType(new Type(((Formal) e.nextElement()).type_decl));

		return t;
	}

	@Override
	public AbstractType visit(Expressions exprs,
			SymbolTable<String, AbstractType> O, Class_ C) {
		ListType t = new ListType();
		for (Enumeration<TreeNode> e = exprs.getElements(); e.hasMoreElements();)
			t.addType(e.nextElement().accept(this, O, C));

		return t;
	}

	@Override
	public AbstractType visit(Cases cases, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
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
		cl.features.accept(this, O, cl);
		return null;
	}

	@Override
	public AbstractType visit(method m, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(attr at, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(Formal formal,
			SymbolTable<String, AbstractType> O, Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(Branch branch,
			SymbolTable<String, AbstractType> O, Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(assign a, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T = O.lookup(a.name);
		if (T == null) {
			classTable.semantError(C).println(a.name + " cannot be resolved");
			return new Type("Object");
		}

		AbstractType T2 = a.expr.accept(this, O, C);

		if (!T2.subType(T, classTable)) {
			classTable.semantError(C).println("type mistmatch");
			return new Type("Object");
		}
		return T2;
	}

	@Override
	public AbstractType visit(static_dispatch sd,
			SymbolTable<String, AbstractType> O, Class_ C) {
		AbstractType T0 = sd.expr.accept(this, O, C);

		method m = classTable.getMethod(sd.type_name, sd.name);
		if (m == null) {
			classTable.semantError(C).println(
					"The method " + sd.name + " is undefined for type "
							+ sd.type_name);
			return new Type("Object");
		}

		AbstractType Tn = sd.actual.accept(this, O, C);
		AbstractType Tnp = m.formals.accept(this, O, C);
		if (!Tn.subType(Tnp, classTable)) {
			classTable.semantError(C).println(
					"Cannot call method " + sd.name
							+ " with the given parameters.");
			return new Type("Object");
		}

		AbstractType Tn1;
		if (m.return_type == "SELF_TYPE")
			Tn1 = T0;
		else
			Tn1 = new Type(m.return_type);

		sd.set_type(Tn1.toString());

		return Tn1;
	}

	@Override
	public AbstractType visit(dispatch d, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T0 = d.expr.accept(this, O, C);

		method m = classTable.methodLookup(T0.getClassForLookup(), d.name);
		if (m == null) {
			classTable.semantError(C).println(
					"The method " + d.name + " is undefined for type " + T0);
			return new Type("Object");
		}

		AbstractType Tn = d.actual.accept(this, O, C);
		AbstractType Tnp = m.formals.accept(this, O, C);

		if (!Tn.subType(Tnp, classTable)) {
			classTable.semantError(C).println(
					"Cannot call method " + d.name
							+ " with the given parameters.");
			return new Type("Object");
		}

		AbstractType Tn1;
		if (m.return_type == "SELF_TYPE")
			Tn1 = T0;
		else
			Tn1 = new Type(m.return_type);

		d.set_type(Tn1.toString());

		return Tn1;

	}

	@Override
	public AbstractType visit(cond c, SymbolTable<String, AbstractType> O,
			Class_ C) {

		AbstractType T1 = c.pred.accept(this, O, C);
		if(!T1.equals("Bool")){
			classTable.semantError(C).println(
					"If predicate must be a boolean value");
			return new Type("Object");
		}
		
		AbstractType T2 = c.then_exp.accept(this, O, C);
		AbstractType T3 = c.else_exp.accept(this, O, C);
		
		String T = classTable.lca(T2, T3);
		
		c.set_type(T);
		
		return new Type(T);
	}

	@Override
	public AbstractType visit(loop l, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(typecase t, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(block b, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType t = b.body.accept(this, O, C);
		if(!(t instanceof ListType))
			Utilities.fatalError("Expected a list of types");
		
		ListType lt = (ListType) t;
		
		AbstractType T = lt.last();
		b.set_type(T.toString());
		
		return T;
	}

	@Override
	public AbstractType visit(let l, SymbolTable<String, AbstractType> O,
			Class_ C) {
		
		
		
		return null;
	}

	@Override
	public AbstractType visit(plus p, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(sub s, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(mul m, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(divide d, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(neg n, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(lt l, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(eq e, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(leq l, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(comp c, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(int_const ic,
			SymbolTable<String, AbstractType> O, Class_ C) {
		return new Type("Int");
	}

	@Override
	public AbstractType visit(bool_const bc,
			SymbolTable<String, AbstractType> O, Class_ C) {
		return new Type("Bool");
	}

	@Override
	public AbstractType visit(string_const sc,
			SymbolTable<String, AbstractType> O, Class_ C) {
		return new Type("String");
	}

	@Override
	public AbstractType visit(new_ n, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType T;
		if (n.type_name.equals("SELF_TYPE"))
			T = new SelfType(C.name);
		else
			T = new Type(n.type_name);

		n.set_type(T.toString());
		return T;
	}

	@Override
	public AbstractType visit(isvoid iv, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(no_expr ne, SymbolTable<String, AbstractType> O,
			Class_ C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType visit(object o, SymbolTable<String, AbstractType> O,
			Class_ C) {
		AbstractType type = O.lookup(o.name);
		if (type != null) {
			o.set_type(type.toString());
			return type;
		}
		classTable.semantError(C).println(o.name + " cannot be resolved");
		return new Type("Object");
	}
}
