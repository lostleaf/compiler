package compiler.absyn;

public class ConstExpr extends Expression {
	public LogOrExpr logOrExpr;

	public ConstExpr(LogOrExpr logOrExpr) {
		super();
		this.logOrExpr = logOrExpr;
	}
}
