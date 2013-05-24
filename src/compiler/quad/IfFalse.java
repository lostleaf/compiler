package compiler.quad;

import compiler.temp.Addr;
import compiler.temp.Label;

public class IfFalse extends Quad {

	public Addr addr = null;
	public Label label = null;

	public IfFalse(Addr a, Label f) {
		label = f;
		addr = a;
	}

	public String toString() {
		return "IfFalse " + addr + " Goto " + label;
	}

}
