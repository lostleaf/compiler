package compiler.env;

import compiler.symbol.Symbol;
import compiler.symbol.Table;
import compiler.temp.Addr;
import compiler.type.CHAR;
import compiler.type.FUNCTION;
import compiler.type.INT;
import compiler.type.POINTER;
import compiler.type.RECORD;
import compiler.type.TYPE;
import compiler.type.VOID;

public final class Env {
	public Table typeEnv = null;
	public Table funcEnv = null;
	public Table idenEnv = null;
	public Table addrEnv = null;

	public Env() {
		initTypeEnv();
		initFuncEnv();
		initIdenEnv();
		initAddrEnv();
	}

	private void initAddrEnv() {
		addrEnv = new Table();
		//TODO add the the system functions 
	}

	private void initIdenEnv() {
		idenEnv = new Table();
	}

	private static Symbol sym(String n) {
		return Symbol.symbol(n);
	}

	private void initTypeEnv() {
		typeEnv = new Table();
		typeEnv.put(sym("int"), INT.getInstance());
		typeEnv.put(sym("char"), CHAR.getInstance());
		typeEnv.put(sym("void"), VOID.getInstance());
	}

	/**
	 * All "library" functions are declared at the outermost level, which does
	 * not contain a frame or formal parameter list.
	 */
	private void initFuncEnv() {
		funcEnv = new Table();
		funcEnv.put(sym("printf"), new FUNCTION(
				new POINTER(CHAR.getInstance()), INT.getInstance(), true, true));
		funcEnv.put(sym("malloc"), new FUNCTION(INT.getInstance(), new POINTER(
				VOID.getInstance()), true, true));
	}

	public void beginScope() {
		funcEnv.beginScope();
		typeEnv.beginScope();
		idenEnv.beginScope();
		addrEnv.beginScope();
	}

	public void endScope() {
		funcEnv.endScope();
		typeEnv.endScope();
		idenEnv.endScope();
		addrEnv.endScope();
	}

	public void putIden(TYPE type, Symbol name) {
		idenEnv.put(name, type);
	}

	public void putType(TYPE type, Symbol name) {
		typeEnv.put(name, type);
	}

	public void putFunc(FUNCTION f, Symbol name) {
		funcEnv.put(name, f);
	}

	public void putAddr(Symbol name, Addr addr) {
		addrEnv.put(name, addr);
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

	public TYPE getByIdenName(Symbol name) {
		Object obj = idenEnv.get(name);
		if (obj == null)
			obj = funcEnv.get(name);
		if (obj == null)
			return null;
		return (TYPE) obj;
	}

	public Addr getAddr(Symbol name) {
		Object obj = addrEnv.get(name);
		if (obj == null)
			return null;
		return (Addr) obj;
	}

}
