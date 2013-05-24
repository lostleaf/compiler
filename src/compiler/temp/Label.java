package compiler.temp;

public class Label implements Addr {
	public static int total = 0;
	int index;

	public Label() {
		index = total++;
	}

	@Override
	public String toString() {
		return "L" + index;
	}
}
