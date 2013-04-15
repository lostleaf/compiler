package compiler.type;

import compiler.symbol.Symbol;

public final class NAME extends TYPE {
	public Symbol symbol;

	public NAME(Symbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public boolean isArray() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRecord() {
		// TODO Auto-generated method stub
		return false;
	}

}
