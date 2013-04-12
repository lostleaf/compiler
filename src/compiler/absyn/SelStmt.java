package compiler.absyn;

public class SelStmt extends Stmt {
	public Expr cond;
	public Stmt thenStmt, elseStmt;

	public SelStmt(Expr cond, Stmt thenStmt, Stmt elseStmt) {
		super();
		this.cond = cond;
		this.thenStmt = thenStmt;
		this.elseStmt = elseStmt;
	}
}
