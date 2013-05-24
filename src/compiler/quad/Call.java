package compiler.quad;

import java.util.List;

import compiler.temp.Addr;
import compiler.temp.Temp;

public class Call extends Quad {
	Temp target;
	Addr source;
	List<Temp> params;

	public Call(Temp target, Addr source, List<Temp> params) {
		this.target = target;
		this.source = source;
		this.params = params;
	}

	public String toString() {
		return target + " = call " + source + " " + params;
	}
}
