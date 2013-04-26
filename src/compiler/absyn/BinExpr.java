package compiler.absyn;

import java.util.List;

public class BinExpr<ExprType> extends Expression {
	public List<ExprType> expr;
	public List<BinOp> op;

	public BinExpr(List<ExprType> expr, List<BinOp> op) {
		super();
		this.expr = expr;
		this.op = op;
	}

}
