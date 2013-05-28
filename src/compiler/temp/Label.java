package compiler.temp;

public class Label implements Addr {
	public static int total = 0;
	public boolean isForFunc = false;
	public int index;

	public Label() {
		index = total++;
	}

	public Label(boolean isForFunc) {
		this();
		this.isForFunc = isForFunc;
	}

	@Override
	public String toString() {
		return "L" + index;
	}
}
