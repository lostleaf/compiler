package compiler.type;

public final class ARRAY extends POINTER {
	int capacity;

	public ARRAY(TYPE type, int cap) {
		super(type);
		capacity = cap;
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
