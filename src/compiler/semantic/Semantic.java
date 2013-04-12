package compiler.semantic;

import java.util.ArrayList;
import java.util.List;

import compiler.absyn.CompStmt;
import compiler.absyn.Declaration;
import compiler.absyn.Declarators;
import compiler.absyn.FunctionDefinition;
import compiler.absyn.InitDeclarators;
import compiler.absyn.Node;
import compiler.absyn.Parameters;
import compiler.absyn.PlainDeclarator;
import compiler.absyn.Program;
import compiler.absyn.TypeSpecifier;
import compiler.env.Env;
import compiler.type.TYPE;
import compiler.util.Error;

public class Semantic {
	private Env env;
	private int loopCount = 0;
	private List<Error> errors = new ArrayList<Error>();

	public Semantic() {
		env = new Env();
	}

	private void error(String message) {
		errors.add(new Error(message, true));
	}

	public void checkProgram(Program program) {
		for (Node node : program.node) {
			if (node instanceof Declaration)
				checkDeclaration((Declaration) node);
			if (node instanceof FunctionDefinition)
				checkFunctionDefinition((FunctionDefinition) node);
		}
	}

	private void checkDeclaration(Declaration decl) {
		TYPE type = checkTypeSpecifier(decl.typeSpecifier);
		if (decl.declarators != null)
			checkDeclarators(decl.declarators, type);
		else if (decl.initDeclarators != null)
			checkInitDeclarators(decl.initDeclarators, type);
	}

	private void checkInitDeclarators(InitDeclarators initDeclarators, TYPE type) {
		// TODO Auto-generated method stub

	}

	private void checkDeclarators(Declarators declarators, TYPE type) {
		// TODO Auto-generated method stub

	}

	private TYPE checkTypeSpecifier(TypeSpecifier typeSpecifier) {
		// TODO Auto-generated method stub
		return null;
	}

	private void checkFunctionDefinition(FunctionDefinition funcDecl) {
		TYPE declType = checkTypeSpecifier(funcDecl.typeSpecifier);

		checkPlainDeclarator(funcDecl.plainDeclarator);

		env.beginScope();
		checkParameters(funcDecl.parameters);
		TYPE retnType = checkCompStmt(funcDecl.compStmt);
		env.endScope();

		if (!matchType(declType, retnType))
			error("declared type and returned type not match");
	}

	private boolean matchType(TYPE declType, TYPE retnType) {
		// TODO Auto-generated method stub
		return true;
	}

	private TYPE checkCompStmt(CompStmt compStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	private void checkParameters(Parameters parameters) {
		// TODO Auto-generated method stub

	}

	private void checkPlainDeclarator(PlainDeclarator plainDeclarator) {
		// TODO Auto-generated method stub

	}

}
