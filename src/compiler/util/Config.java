package compiler.util;

public interface Config {
	// ADD, SUB, MUL, DIV, MOD, LEFTSHIFT, RIGHTSHIFT, LESS,
	// LEQ, GREATER, GEQ, EQ, NEQ, ASSIGN, MULASSIGN, DIVASSIGN,
	// MODASSIGN, ADDASSIGN, SUBASSIGN, LSHASSIGN, RSHASSIGN,
	// ANDASSIGN, XORASSIGN, ORASSIGN

	public static String bOpStr[] = { "+", "-", "*", "/", "%", "<<", ">>", "<",
			"<=", ">", ">=", "==", "!=", "=", "*=", "/=", "%=", "+=", "-=",
			"<<=", ">>=", "+=", "^=", "|=" };

	static final String[] bOpAsm = { "add", "sub", "mul", "div", "rem", "sll",
			"srl", "slt", "sle", "sgt", "sge", "seq", "sne", };

	// AND,STAR,PLUS,MINUS,TILDE,NOT
	public static String uOpStr[] = { "&", "*", "+", "-", "~", "!" };

	static final String[] regNames = { "zero", "at", "v0", "v1", "a0", "a1",
			"a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0",
			"s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8", "t9", "k0", "k1",
			"gp", "sp", "fp", "ra", "spill" // special register
	};
	
	static final String[] ifFalseCmp = { "", "", "", "", "ne", "eq", "ge",
		"gt", "le", "lt" };

	static final int spillReg = regNames.length - 1;
	static final int wordSize = 4;

	static int savedRegistersOffset = 2; // old $ra, old display
	static int baseOfSavedRegisters = 8; // start from $t0
	static int numOfSavedRegisters = 18;
	
	static final int paramRegBase = 4; // start from $a0
	static final int paramRegNum = 4; // $a0-$a3
}
