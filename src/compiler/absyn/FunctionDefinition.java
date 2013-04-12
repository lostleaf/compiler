package compiler.absyn;
public class FunctionDefinition extends Node{
	public TypeSpecifier ts;
	public PlainDeclarator pd;
	public Parameters p;
	public CompStmt cs;
	public FunctionDefinition(TypeSpecifier t, PlainDeclarator pdd, Parameters pp, CompStmt c){
		ts=t;pd=pdd;p=pp;cs=c;
	}
}
