package compiler.quad;

import compiler.temp.Temp;

public class Return extends Quad{

	private Temp addr = null;

	public Return(Temp a) {
		addr = a;
	}

	@Override
	public String toString() {
		return "return " + addr.toString();
	}
}
