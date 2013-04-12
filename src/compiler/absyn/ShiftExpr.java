package compiler.absyn;

import java.util.List;

public class ShiftExpr extends BinExpr<AddExpr> {

	public ShiftExpr(List<AddExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
