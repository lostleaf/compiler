package compiler.util;

import compiler.absyn.BinOp;

public class Converter implements Config {
	
	public static BinOp strToBinOp(String op) {
		for (int i = 0; i < opStr.length; i++)
			if (opStr[i].equals(op))
				return BinOp.values()[i];
		return null;
	}
}
