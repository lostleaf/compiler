package compiler.absyn;


public class PlainDeclaration extends Node {
	public TypeSpecifier type;
	public Declarator declarator;

	public PlainDeclaration(TypeSpecifier type, Declarator declarator) {
		super();
		this.type = type;
		this.declarator = declarator;
	}
}
