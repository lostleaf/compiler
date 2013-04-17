package compiler.type;

public class POINTER extends TYPE {
	public TYPE elementType;

	public POINTER(TYPE type) {
		this.elementType = type;
	}

	@Override
	public boolean isArray() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRecord() {
		// TODO Auto-generated method stub
		return false;
	}

}
