package compiler.absyn;

import java.util.List;

public class RelExpr extends BinExpr<ShiftExpr> {

	public RelExpr(List<ShiftExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
