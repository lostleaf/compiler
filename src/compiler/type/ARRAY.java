package compiler.type;

import compiler.temp.Addr;
import compiler.temp.IntConstant;

public final class ARRAY extends POINTER {

	public ARRAY(TYPE type, int size) {
		this(type, new IntConstant(size));
	}

	public ARRAY(TYPE type, Addr totalSize) {
		super(type);
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
