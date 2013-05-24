package compiler.quad;

import compiler.temp.Addr;

public class Move extends Quad {
	public Addr target, source;

	public Move() {
		this(null, null);
	}

	public Move(Addr target, Addr source) {
		this.target = target;
		this.source = source;
	}

	public String toString() {
		return target.toString() + " = " + source.toString();
	}
}
