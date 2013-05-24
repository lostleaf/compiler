package compiler.util;

public interface Config {
	// ADD, SUB, MUL, DIV, MOD, LEFTSHIFT, RIGHTSHIFT, LESS,
	// LEQ, GREATER, GEQ, EQ, NEQ, ASSIGN, MULASSIGN, DIVASSIGN,
	// MODASSIGN, ADDASSIGN, SUBASSIGN, LSHASSIGN, RSHASSIGN,
	// ANDASSIGN, XORASSIGN, ORASSIGN

	public static String bOpStr[] = { "+", "-", "*", "/", "%", "<<", ">>",
			"<", "<=", ">", ">=", "==", "!=", "=", "*=", "/=", "%=", "+=",
			"-=", "<<=", ">>=", "+=", "^=", "|=" };

	// AND,STAR,PLUS,MINUS,TILDE,NOT
	public static String uOpStr[] = { "&", "*", "+", "-", "~", "!" };
}
