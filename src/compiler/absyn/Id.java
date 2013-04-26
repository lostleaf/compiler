package compiler.absyn;

import compiler.symbol.Symbol;

public class Id extends Expression {
	public Symbol symbol;

	public Id(Symbol sym) {
		symbol = sym;
	}
}
