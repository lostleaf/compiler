package compiler.absyn;

public class ArrPostfix extends Postfix {
	public Expr expression;

	public ArrPostfix(Expr e) {
		this.expression = e;
	}
}
