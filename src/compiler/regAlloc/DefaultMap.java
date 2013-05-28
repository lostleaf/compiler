package compiler.regAlloc;

import compiler.analysis.LiveInterval;
import compiler.temp.Temp;

public class DefaultMap implements RegAlloc {

	@Override
	public String map(Temp t) {
		LiveInterval i = t.getLiveInterval();
		return "$" + regNames[i.register];
	}
	
	private static DefaultMap instance = null;

	public static DefaultMap getSingleton() {
		if (instance == null) {
			instance = new DefaultMap();
		}
		return instance;
	}
}
