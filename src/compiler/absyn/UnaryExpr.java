package compiler.absyn;

public class UnaryExpr extends Expression {
	final public static int POSTEXP = 0; // postfix_expression
	final public static int PREINC = 1; // '++' unary_expression
	final public static int PREDEC = 2; // '--' unary_expression
	final public static int UNARYOP = 3; // unary_operator cast_expression
	final public static int SIZEUEXP = 4; // 'sizeof' unary_expression
	final public static int SIZETYNAME = 5; // 'sizeof' '(' type_name ')'
	public int exprType = -1;
	public Expression expr;
	public UnaryOp op;

	public UnaryExpr(int type, Expression expr) {
		super();
		this.exprType = type;
		this.expr = expr;
	}

	public UnaryExpr(int type, UnaryOp op, Expression expr) {
		this(type, expr);
		if (type != UnaryExpr.UNARYOP)
			System.err.println("Warning: [absyn.UnaryExpr] wrong type");
		this.op = op;
	}
}
