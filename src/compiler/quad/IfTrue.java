package compiler.quad;

import compiler.temp.Addr;
import compiler.temp.Label;

public class IfTrue extends Quad {

	public Addr addr = null;
	public Label label = null;

	public IfTrue(Addr a, Label f) {
		label = f;
		addr = a;
	}

	public String toString() {
		return "IfTrue " + addr + " Goto " + label;
	}

}
