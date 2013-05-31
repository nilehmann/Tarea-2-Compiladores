
public interface ASTVisitor<R, P1, P2> {
	/*Lists*/
	public R visit(Classes classes, P1 arg1, P2 arg2);
	public R visit(Features features, P1 arg1, P2 arg2);
	public R visit(Formals formals, P1 arg1, P2 arg2);
	public R visit(Expressions exprs, P1 arg1, P2 arg2);
	public R visit(Cases cases, P1 arg1, P2 arg2);	
	
	public R visit(Program program, P1 arg1, P2 arg2);
	
	public R visit(Class_ class_, P1 arg1, P2 arg2);
	public R visit(method m, P1 arg1, P2 arg2);
	public R visit(attr at, P1 arg1, P2 arg2);
	public R visit(Formal formal, P1 arg1, P2 arg2);
	
	public R visit(Branch branch, P1 arg1, P2 arg2);
	
	/*Expressions*/
	public R visit(assign a, P1 arg1, P2 arg2);
	public R visit(static_dispatch sd, P1 arg1, P2 arg2);
	public R visit(dispatch d, P1 arg1, P2 arg2);
	public R visit(cond c, P1 arg1, P2 arg2);
	public R visit(loop l, P1 arg1, P2 arg2);
	public R visit(typecase t, P1 arg1, P2 arg2);
	public R visit(block b, P1 arg1, P2 arg2);
	public R visit(let l, P1 arg1, P2 arg2);
	public R visit(plus p, P1 arg1, P2 arg2);
	public R visit(sub s, P1 arg1, P2 arg2);
	public R visit(mul m, P1 arg1, P2 arg2);
	public R visit(divide d, P1 arg1, P2 arg2);
	public R visit(neg n, P1 arg1, P2 arg2);
	public R visit(lt l, P1 arg1, P2 arg2);
	public R visit(eq e, P1 arg1, P2 arg2);
	public R visit(leq l, P1 arg1, P2 arg2);
	public R visit(comp c, P1 arg1, P2 arg2);
	public R visit(int_const ic, P1 arg1, P2 arg2);
	public R visit(bool_const bc, P1 arg1, P2 arg2);
	public R visit(string_const sc, P1 arg1, P2 arg2);
	public R visit(new_ n, P1 arg1, P2 arg2);
	public R visit(isvoid iv, P1 arg1, P2 arg2);
	public R visit(no_expr ne, P1 arg1, P2 arg2);
	public R visit(object o, P1 arg1, P2 arg2);
	
}
