package compiler.absyn;

public class PriExpr extends Expression {
	public Expression expr;

	public PriExpr(Expression exp) {
		super();
		this.expr = exp;
	}
	
}
