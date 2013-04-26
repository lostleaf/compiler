package compiler.absyn;

public class ReturnStmt extends JumpStmt {
	public Expr expr;

	public ReturnStmt(Expr expr) {
		super();
		this.expr = expr;
	}
}
