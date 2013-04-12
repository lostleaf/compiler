package compiler.absyn;

public class ExprStmt extends Stmt {
	public Expr expr;

	public ExprStmt(Expr expr) {
		super();
		this.expr = expr;
	}
}
