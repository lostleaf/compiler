package compiler.env;

import compiler.symbol.Symbol;
import compiler.symbol.Table;
import compiler.type.CHAR;
import compiler.type.INT;
import compiler.type.RECORD;
import compiler.type.TYPE;
import compiler.type.VOID;

public final class Env {
	public Table typeEnv = null;
	public Table funcEnv = null;

	public Env() {
		initTEnv();
		initVEnv();
	}

	private static Symbol sym(String n) {
		return Symbol.symbol(n);
	}

	private void initTEnv() {
		typeEnv = new Table();
		typeEnv.put(sym("int"), INT.getInstance());
		typeEnv.put(sym("char"), CHAR.getInstance());
		typeEnv.put(sym("void"), VOID.getInstance());
	}

	/**
	 * All "library" functions are declared at the outermost level, which does
	 * not contain a frame or formal parameter list.
	 */
	private void initVEnv() {
		funcEnv = new Table();
	}

	public void beginScope() {
		funcEnv.beginScope();
		typeEnv.beginScope();
	}

	public void endScope() {
		funcEnv.endScope();
		typeEnv.endScope();
	}

	public TYPE getByTypedefName(Symbol name) {
		Object obj = typeEnv.get(name);
		if (obj == null)
			return null;
		return (TYPE) obj;
	}

	public RECORD getByRecordName(Symbol name) {
		Object obj = typeEnv.get(name);
		if (obj == null)
			return null;
		if (!(obj instanceof RECORD)) {
			// Semantic.getCurrent().error(name.toString() + " not a record");
			System.err.println("warning" + name.toString() + "not a record");
			return null;
		}
		return (RECORD) obj;
	}
}
