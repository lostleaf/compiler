package compiler.quad;

import compiler.temp.Label;

public class Leave extends Quad {

	private Label label = null;

	public Leave(Label l) {
		label = l;
	}

	public String toString() {
		return "leave " + label;
	}
}
