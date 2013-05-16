package compiler.type;

import compiler.temp.Addr;
import compiler.temp.IntConstant;

public final class ARRAY extends POINTER {
	public Addr capacity;

	public ARRAY(TYPE type, int cap) {
		this(type, new IntConstant(cap));
	}

	public ARRAY(TYPE type, Addr c) {
		this(type, c, c);
	}

	public ARRAY(TYPE type, Addr capacity, Addr totalSize) {
		super(type);
		this.capacity = capacity;
		this.size = totalSize;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ARRAY) {
			return elementType.equals(((ARRAY) other).elementType);
		}
		return false;
	}

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public boolean isRecord() {
		return false;
	}

}
