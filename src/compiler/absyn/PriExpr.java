package compiler.absyn;

public class PriExpr extends Expression {
	public Expression exp;

	public PriExpr(Expression exp) {
		super();
		this.exp = exp;
	}
	
}
