package compiler.absyn;

import java.util.List;

public class AndExpr extends BinExpr<EquExpr> {

	public AndExpr(List<EquExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
