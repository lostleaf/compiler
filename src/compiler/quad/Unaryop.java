package compiler.quad;

import compiler.absyn.UnaryOp;
import compiler.temp.Addr;
import compiler.temp.Temp;
import compiler.util.Config;

public class Unaryop extends Quad implements Config {
	public Temp dest;
	public Addr left;
	public UnaryOp op;

	public Unaryop(Temp dest, UnaryOp op, Temp left) {
		this.dest = dest;
		this.op = op;
		this.left = left;
	}

	public String toString() {
		return dest + " = " + uOpStr[op.ordinal()] + " " + left;
	}
}