package compiler.type;

import compiler.temp.IntConstant;

public class POINTER extends TYPE {
	public TYPE elementType;

	public POINTER(TYPE type) {
		elementType = type;
		size = new IntConstant(4);
	}

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof POINTER)
			return this.elementType.equals(((POINTER) o).elementType);
		return false;
	}

	@Override
	public boolean isRecord() {
		return false;
	}

}
