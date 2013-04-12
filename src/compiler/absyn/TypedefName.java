package compiler.absyn;

import compiler.symbol.Symbol;

public class TypedefName extends Node {
	public Symbol name = null;

	public TypedefName(Symbol name) {
		super();
		this.name = name;
	}

}
