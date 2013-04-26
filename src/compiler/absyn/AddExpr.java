package compiler.absyn;

import java.util.List;

public class AddExpr extends BinExpr<MulExpr> {

	public AddExpr(List<MulExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
