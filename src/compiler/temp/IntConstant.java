package compiler.temp;

public class IntConstant implements Addr {
	public int value;

	public IntConstant(int v) {
		value = v;
	}

	@Override
	public IntConstant clone() {
		return new IntConstant(value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
