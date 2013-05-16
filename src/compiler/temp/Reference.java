package compiler.temp;

public class Reference implements Addr {
	public Temp base;
	public Addr offset;

	public Reference(Temp base) {
		this(base, null);
	}

	public Reference(Temp temp, Addr addr) {
		base = temp;
		offset = addr;
	}

	@Override
	public String toString() {
		return base.toString() + "[" + offset.toString() + "]";
	}
}