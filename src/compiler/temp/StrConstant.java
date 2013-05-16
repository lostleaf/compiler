package compiler.temp;

public class StrConstant extends Const {
	String value;

	public StrConstant(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return new String(value);
	}
}
