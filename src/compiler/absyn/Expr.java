package compiler.absyn;

import java.util.List;

public class Expr extends Expression {
	public List<AssExpr> assExpr;

	public Expr(List<AssExpr> assExpr) {
		super();
		this.assExpr = assExpr;
	}

}
