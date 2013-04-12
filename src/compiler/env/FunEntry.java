package compiler.env;

import compiler.type.RECORD;
import compiler.type.TYPE;

public class FunEntry implements Entry {
	public RECORD formals = null;
	public TYPE result = null;

	public FunEntry(RECORD f, TYPE r) {
		formals = f;
		result = r;
	}

}
