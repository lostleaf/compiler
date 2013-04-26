package compiler.type;

public class POINTER extends TYPE {
	public TYPE elementType;

	public POINTER(TYPE type) {
		this.elementType = type;
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
