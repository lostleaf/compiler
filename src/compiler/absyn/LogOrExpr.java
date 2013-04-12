package compiler.absyn;

import java.util.List;

public class LogOrExpr extends BinExpr<LogAndExpr> {

	public LogOrExpr(List<LogAndExpr> expr, List<BinOp> op) {
		super(expr, op);
	}

}
