package compiler.temp;

public class Temp implements Addr{
	public static int total = 0;
	public int index;

	public Temp() {
		index = total++;
	}

	@Override
	public String toString() {
		return "T" + index;
	}
}
