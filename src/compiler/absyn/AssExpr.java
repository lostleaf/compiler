package compiler.absyn;

public class AssExpr extends Expression {
	public LogOrExpr logOrExpr;
	public UnaryExpr unaryExpr;
	public BinOp op;
	public AssExpr assExpr;

	public AssExpr(LogOrExpr logOrExpr, UnaryExpr unaryExpr, BinOp op,
			AssExpr assExpr) {
		super();
		this.logOrExpr = logOrExpr;
		this.unaryExpr = unaryExpr;
		this.op = op;
		this.assExpr = assExpr;
	}
}
