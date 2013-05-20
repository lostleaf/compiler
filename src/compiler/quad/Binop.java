package compiler.quad;

import compiler.absyn.BinOp;
import compiler.temp.Addr;
import compiler.temp.Temp;
import compiler.util.Config;

public class Binop extends Quad implements Config {

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