package compiler.quad;

import compiler.temp.Label;

public class LabelQuad extends Quad {
	Label label;

	public LabelQuad(Label l) {
		label = l;
	}

	public LabelQuad() {
		this(new Label());
	}

	@Override
	public String toString() {
		return label + ":";
	}

}
