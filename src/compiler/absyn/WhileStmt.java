package compiler.absyn;

public class WhileStmt extends IterStmt {

	public WhileStmt(Expr cond, Stmt stmt) {
		super();
		this.cond = cond;
		this.stmt = stmt;
	}

}
