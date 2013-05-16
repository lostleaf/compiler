package compiler.quad;

import compiler.absyn.BinOp;
import compiler.temp.Addr;
import compiler.temp.Temp;

public class Binop extends Quad {
	// ADD, SUB, MUL, DIV, MOD, LEFTSHIFT, RIGHTSHIFT, LESS,
	// LEQ, GREATER, GEQ, EQ, NEQ, ASSIGN, MULASSIGN, DIVASSIGN,
	// MODASSIGN, ADDASSIGN, SUBASSIGN, LSHASSIGN, RSHASSIGN,
	// ANDASSIGN, XORASSIGN, ORASSIGN

	private static String opStr[] = { "+", "-", "*", "/", "%", "<<", ">>", "<",
			"<=", ">", ">=", "==", "!=", "=", "*=", "/=", "%=", "+=", "-=",
			"<<=", ">>=", "+=", "^=", "|=" };

	public Temp target;
	public Addr addr1, addr2;
	public BinOp op;

	public Binop(Temp target, Addr addr1, Addr addr2, BinOp op) {
		this.target = target;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.op = op;
	}

	public String toString() {
		return target.toString() + " = " + addr1.toString() + " "
				+ opStr[op.ordinal()] + " " + addr2.toString();
	}

}