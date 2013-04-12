package compiler.type;

public final class NAME extends TYPE {
	String name;

	public NAME(String name) {
		this.name = name;
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
