package compiler.absyn;

public class ForStmt extends IterStmt {
	public Expr begin, cond, end;
	public Stmt stmt;
	public ForStmt(Expr begin, Expr cond, Expr end, Stmt stmt) {
		super();
		this.begin = begin;
		this.cond = cond;
		this.end = end;
		this.stmt = stmt;
	}
	
}
