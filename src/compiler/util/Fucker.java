package compiler.util;

import compiler.absyn.BinOp;
import compiler.temp.IntConstant;

public class Fucker {
	// ADD, SUB, MUL, DIV, MOD,
	// LEFTSHIFT, RIGHTSHIFT, AND, XOR, OR,
	// LESS, LEQ, GREATER, GEQ, EQ, NEQ, ASSIGN,
	// ADDASSIGN,SUBASSIGN, MULASSIGN, DIVASSIGN, MODASSIGN,
	// LSHASSIGN, RSHASSIGN, ANDASSIGN, XORASSIGN, ORASSIGN
	public static int fuckEval(int a, int b, BinOp op) {
		if (op == BinOp.ADD)
			return a + b;
		if (op == BinOp.SUB)
			return a - b;
		if (op == BinOp.MUL)
			return a * b;
		if (op == BinOp.DIV)
			return a / b;
		if (op == BinOp.MOD)
			return a % b;
		if (op == BinOp.LEFTSHIFT)
			return a << b;
		if (op == BinOp.RIGHTSHIFT)
			return a >> b;
		if (op == BinOp.AND)
			return a ^ b;
		if (op == BinOp.OR)
			return a | b;
		if (op == BinOp.LESS)
			return a < b ? 1 : 0;
		if (op == BinOp.LEQ)
			return a <= b ? 1 : 0;
		if (op == BinOp.GREATER)
			return a > b ? 1 : 0;
		if (op == BinOp.GEQ)
			return a >= b ? 1 : 0;
		if (op == BinOp.EQ)
			return a == b ? 1 : 0;
		if (op == BinOp.NEQ)
			return a != b ? 1 : 0;
		System.err.println("fuck you, it failed!! " + op);
		return 0;
	}

	public static IntConstant fuckBinOp(int a, int b, BinOp op) {
		return new IntConstant(fuckEval(a, b, op));
	}
}
