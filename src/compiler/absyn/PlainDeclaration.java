package compiler.absyn;

import java.util.List;

public class PlainDeclaration extends Node {
	public TypeSpecifier type;
	public List<Declarator> declarator;

	public PlainDeclaration(TypeSpecifier type, List<Declarator> declarator) {
		super();
		this.type = type;
		this.declarator = declarator;
	}
}
