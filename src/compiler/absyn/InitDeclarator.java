package compiler.absyn;

public class InitDeclarator extends Node {
	public InitDeclarator(Declarator declarator, Initializer initializer) {
		super();
		this.declarator = declarator;
		this.initializer = initializer;
	}
	public Declarator declarator;
	public Initializer initializer;
}
