package compiler.absyn;

import java.util.List;

public class ExOrExpr extends BinExpr<AndExpr> {

	public ExOrExpr(List<AndExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
