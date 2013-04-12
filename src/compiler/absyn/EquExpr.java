package compiler.absyn;

import java.util.List;

public class EquExpr extends BinExpr<RelExpr> {

	public EquExpr(List<RelExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
