package compiler.type;

import compiler.symbol.Symbol;
import compiler.temp.Addr;

public abstract class TYPE {

	public static final RECORD NULL = new RECORD(Symbol.symbol("(null)"));
	
	public boolean isNull(){
		return this == NULL;
	}
	
	public abstract boolean isArray();
	
	public abstract boolean isRecord();
	
	public Addr size;
}
