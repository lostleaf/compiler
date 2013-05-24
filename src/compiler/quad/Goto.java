package compiler.quad;

import compiler.temp.Label;

public class Goto extends Quad {
	public Label label = null;

	public Goto(Label l) {
		label = l;
	}

	public String toString() {
		return "Goto " + label;
	}
}
