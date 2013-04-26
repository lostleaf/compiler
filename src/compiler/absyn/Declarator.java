package compiler.absyn;

public class Declarator extends Node {
	public PlainDeclarator plainDeclarator;

	public Declarator(PlainDeclarator plainDeclarator) {
		super();
		this.plainDeclarator = plainDeclarator;
	}
}
