package compiler.quad;

import compiler.temp.Addr;
import compiler.temp.Label;
import compiler.temp.Temp;

public class Branch extends Quad {

	public static int EQUAL = 0, UNEQUAL = 1;

	private int type;
	private Temp left;
	private Addr right;
	private Label label;

	public Branch(int type, Temp left, Addr right, Label label) {
		this.type = type;
		this.left = left;
		this.right = right;
		this.label = label;
	}

	public String toString() {
		return "b" + (type == 0 ? "=" : "!=") + " " + left + " " + right
				+ " goto " + label;
	}

}
