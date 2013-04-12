package compiler.absyn;

public class ArrPostfix extends Postfix {
	public Expression expression;

	public ArrPostfix(Expression e) {
		this.expression = e;
	}
}
