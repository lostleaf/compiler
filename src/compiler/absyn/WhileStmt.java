package compiler.absyn;

public class WhileStmt extends IterStmt {
	public Expr cond;
	public Stmt stmt;

	public WhileStmt(Expr cond, Stmt stmt) {
		super();
		this.cond = cond;
		this.stmt = stmt;
	}

}
