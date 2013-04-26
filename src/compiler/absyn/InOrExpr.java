package compiler.absyn;

import java.util.List;

public class InOrExpr extends BinExpr<ExOrExpr> {

	public InOrExpr(List<ExOrExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
