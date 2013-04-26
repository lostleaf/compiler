package compiler.absyn;

import java.util.List;

public class Declarators extends Node {
	public List<Declarator> declarator = null;

	public Declarators(List<Declarator> declarator) {
		super();
		this.declarator = declarator;
	}

}
