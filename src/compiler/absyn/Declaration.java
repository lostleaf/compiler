package compiler.absyn;

public class Declaration extends Node {
	TypeSpecifier typeSpecifier = null;
	Declarators declarators = null;
	InitDeclarators initDeclarators = null;

	public Declaration(TypeSpecifier t, Declarators d) {
		typeSpecifier = t;
		declarators = d;
	}

	public Declaration(TypeSpecifier t, InitDeclarators i) {
		typeSpecifier = t;
		initDeclarators = i;
	}
}
