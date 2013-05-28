package compiler.temp;

public class Reference implements Addr {
	public Temp base;
	public IntConstant offset;

	public Reference(Temp base) {
		this(base, new IntConstant(0));
	}

	public Reference(IntConstant i) {
		this(null, i);
	}

	public Reference(Temp base, IntConstant offset) {
		this.base = base;
		this.offset = offset;
	}

	@Override
	public String toString() {
		return base.toString() + "[" + offset.toString() + "]";
	}
}
