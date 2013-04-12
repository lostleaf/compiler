package compiler.absyn;

public class Declaration extends Node {
	public TypeSpecifier typeSpecifier = null;
	public Declarators declarators = null;
	public InitDeclarators initDeclarators = null;

	public Declaration(TypeSpecifier t, Declarators d) {
		typeSpecifier = t;
		declarators = d;
	}

	public Declaration(TypeSpecifier t, InitDeclarators i) {
		typeSpecifier = t;
		initDeclarators = i;
	}
}
