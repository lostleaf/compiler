package compiler.temp;

import compiler.analysis.LiveInterval;
import compiler.translate.Level;

public class Temp implements Addr {
	public static int total = 0;
	public static Temp sp = new Temp(), gp = new Temp(), v1 = new Temp();

	public int index;
	public Level level;

	public Temp() {
		index = total++;
	}

	public Temp(Level l, int i) {
		index = total++;
		level = l;
		index = i;
	}

	@Override
	public String toString() {
		if (this.equals(sp))
			return "sp";
		if (this.equals(gp))
			return "gp";
		if (this.equals(v1))
			return "v1";
		return "T" + index;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Temp) {
			return index == ((Temp) other).index;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return index;
	}

	private LiveInterval interval = null;

	public void expandInterval(int qcount) {
		if (interval == null) {
			interval = new LiveInterval(this, qcount);
		}
		interval.insert(qcount);
	}

	public LiveInterval getLiveInterval() {
		return interval;
	}

	public void clearLiveInterval() {
		interval = null;
	}
}
