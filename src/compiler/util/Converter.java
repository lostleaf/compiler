package compiler.util;

import compiler.absyn.BinOp;

public class Converter implements Config {
	
	public static BinOp strToBinOp(String op) {
		for (int i = 0; i < bOpStr.length; i++)
			if (bOpStr[i].equals(op))
				return BinOp.values()[i];
		return null;
	}
}
