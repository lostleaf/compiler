package compiler.temp;

public class IntConstant extends Const {
	public int value;

	public IntConstant(int v) {
		value = v;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
