package compiler.quad;

import java.util.List;

import compiler.temp.Label;
import compiler.temp.Temp;

public class Enter extends Quad {

	Label label = null;

	List<Temp> params;

	public Enter(Label label, List<Temp> params) {
		this.label = label;
		this.params = params;
	}

	public String toString() {
		return "Enter " + label;
	}

}
