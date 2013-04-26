package compiler.absyn;

import java.util.List;

public class Initializer extends Node {
	public AssExpr assExpr;
	public List<Initializer> initializer;

	public Initializer(AssExpr assExpr, List<Initializer> initializer) {
		super();
		this.assExpr = assExpr;
		this.initializer = initializer;
	}

}
