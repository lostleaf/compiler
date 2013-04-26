package compiler.absyn;

import java.util.List;

public class MulExpr extends BinExpr<CastExpr> {

	public MulExpr(List<CastExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
