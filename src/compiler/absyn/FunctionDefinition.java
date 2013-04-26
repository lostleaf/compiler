package compiler.absyn;

public class FunctionDefinition extends Node {
	public TypeSpecifier typeSpecifier;
	public PlainDeclarator plainDeclarator;
	public Parameters parameters;
	public CompStmt compStmt;

	public FunctionDefinition(TypeSpecifier t, PlainDeclarator pdd,
			Parameters pp, CompStmt c) {
		typeSpecifier = t;
		plainDeclarator = pdd;
		parameters = pp;
		compStmt = c;
	}
}
