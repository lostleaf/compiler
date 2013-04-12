package compiler.absyn;

public class CastExpr extends Expression {
	public TypeName typeName;
	public Expression expression;

	public CastExpr(TypeName typeName, Expression castExpr) {
		super();
		this.typeName = typeName;
		this.expression = castExpr;
	}
}
