package compiler.type;

public class POINTER extends TYPE {
	TYPE type;

	public POINTER(TYPE type) {
		this.type = type;
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
