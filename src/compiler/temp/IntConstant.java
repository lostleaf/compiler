package compiler.temp;

public class IntConstant extends Const {
	int value;

	public IntConstant(int v) {
		value = v;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
