package compiler.temp;

import compiler.analysis.LiveInterval;
import compiler.translate.Level;

public class Temp implements Addr {
	public static int total = 0;
	public static Temp sp = new Temp(null, 0), gp = new Temp(null, 0),
			v1 = new Temp(null, 0);

	public int num, index;
	public Level level;

	public Temp(Level l, int i) {
		this.num = total++;
		this.level = l;
		this.index = i;
	}

	public Object getName() {
		if (this.equals(sp))
			return "$sp";
		if (this.equals(gp))
			return "$gp";
		if (this.equals(v1))
			return "$v1";
		return this;
	}

	@Override
	public String toString() {
		if (this.equals(sp))
			return "sp";
		if (this.equals(gp))
			return "gp";
		if (this.equals(v1))
			return "v1";
		return "T" + num;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Temp ? num == ((Temp) other).num : false;
	}

	@Override
	public int hashCode() {
		return num;
	}

	private LiveInterval interval = null;

	public void expandInterval(int qcount) {
		if (interval == null)
			interval = new LiveInterval(this, qcount);
		interval.insert(qcount);
	}

	public LiveInterval getLiveInterval() {
		return interval;
	}

	public void clearLiveInterval() {
		interval = null;
	}
}
