package compiler.analysis;

import compiler.temp.Temp;

public class Expression {

	public String exp;
	public Temp dst;
	
	public Expression(String e, Temp d) {
		exp = e;
		dst = d;
	}

	public boolean isKilledBy(Temp t) {
		return exp.contains("(" + t + ")");
	}
	
	public String toString() {
		return exp + " => " + dst;
	}
}
