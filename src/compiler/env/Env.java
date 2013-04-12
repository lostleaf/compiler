package compiler.env;

import compiler.symbol.Symbol;
import compiler.symbol.Table;
import compiler.type.CHAR;
import compiler.type.INT;
import compiler.type.VOID;



public final class Env {
	public Table tEnv = null;
	public Table vEnv = null;
	
	public Env() {
		initTEnv();
		initVEnv();
	}

	private static Symbol sym(String n) {
		return Symbol.symbol(n);
	}
	
	private void initTEnv() {
		tEnv = new Table();
		tEnv.put(sym("int"), INT.getInstance());
		tEnv.put(sym("char"), CHAR.getInstance());
		tEnv.put(sym("void"), VOID.getInstance());
	}

	/**
	 * All "library" functions are declared at the outermost level,
	 * which does not contain a frame or formal parameter list.
	 */
	private void initVEnv() {
		vEnv = new Table();
	}
	
	public void beginScope() {
		vEnv.beginScope();
		tEnv.beginScope();
	}
	
	public void endScope() {
		vEnv.endScope();
		tEnv.endScope();
	}
}
