package compiler.absyn;

import java.util.List;

public class LogAndExpr extends BinExpr<InOrExpr> {

	public LogAndExpr(List<InOrExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
