package compiler;

import compiler.absyn.BinOp;

public class Test {
	public static void main(String args[]) {
		System.out.println(BinOp.ADD.ordinal() + " "
				+ BinOp.ADDASSIGN.ordinal());
		System.out.println(BinOp.DIV.ordinal() + " "
				+ BinOp.DIVASSIGN.ordinal());
	}
}
