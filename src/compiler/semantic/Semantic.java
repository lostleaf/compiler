package compiler.semantic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import compiler.absyn.ArrDeclarator;
import compiler.absyn.AssExpr;
import compiler.absyn.CharType;
import compiler.absyn.CompStmt;
import compiler.absyn.ConstExpr;
import compiler.absyn.Declaration;
import compiler.absyn.Declarator;
import compiler.absyn.Declarators;
import compiler.absyn.FunDeclarator;
import compiler.absyn.FunctionDefinition;
import compiler.absyn.Id;
import compiler.absyn.InitDeclarator;
import compiler.absyn.InitDeclarators;
import compiler.absyn.Initializer;
import compiler.absyn.IntType;
import compiler.absyn.NameType;
import compiler.absyn.Node;
import compiler.absyn.Parameters;
import compiler.absyn.PlainDeclaration;
import compiler.absyn.PlainDeclarator;
import compiler.absyn.Program;
import compiler.absyn.RecordType;
import compiler.absyn.TypeSpecifier;
import compiler.absyn.VoidType;
import compiler.builder.ArrayBuilder;
import compiler.builder.FunctionBuilder;
import compiler.env.Env;
import compiler.symbol.Symbol;
import compiler.type.CHAR;
import compiler.type.INT;
import compiler.type.NAME;
import compiler.type.RECORD;
import compiler.type.TYPE;
import compiler.type.VOID;
import compiler.util.Error;
import compiler.util.Pair;

public class Semantic {
	private Env env;
	private int loopCount = 0;
	private List<Error> errors = new ArrayList<Error>();
	private static Semantic cur;

	public Semantic() {
		env = new Env();
		cur = this;
	}

	public static Semantic getCurrent() {
		return cur;
	}

	public void error(String message) {
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
		for (InitDeclarator initd : initDeclarators.initDecl)
			checkInitDeclarator(initd, type);
	}

	private Pair<TYPE, Symbol> checkInitDeclarator(InitDeclarator initd,
			TYPE type) {
		Pair<TYPE, Symbol> pair = checkDeclarator(initd.declarator, type);
		if (initd.initializer != null) {
			TYPE fromType = checkInitializer(initd.initializer);
			if (!canCast(fromType, pair.first))
				error("cannnot cast type in initial declaration");
		}
		return pair;
	}

	private TYPE checkInitializer(Initializer initializer) {
		// TODO correct the initializer definition
		return null;
	}

	private void checkDeclarators(Declarators declarators, TYPE type) {
		// TODO Auto-generated method stub
		for (Declarator decl : declarators.declarator) {
			checkDeclarator(decl, type);
		}
	}

	private Pair<TYPE, Symbol> checkDeclarator(Declarator decl, TYPE type) {
		if (decl instanceof FunDeclarator)
			return checkFuncDeclarator((FunDeclarator) decl, type);
		if (decl instanceof ArrDeclarator)
			return checkArrDeclarator((ArrDeclarator) decl, type);
		System.err.println("Declarator is neither Func nor Arr");
		return null;
	}

	private Pair<TYPE, Symbol> checkArrDeclarator(ArrDeclarator decl, TYPE t) {
		int starCount = decl.plainDeclarator.starCount;
		Id id = decl.plainDeclarator.id;

		for (ConstExpr constExpr : decl.constExpr) {
			TYPE type = checkConstExpr(constExpr);
			if (type != INT.getInstance() && type != CHAR.getInstance())
				error("length of array must be integer");
		}
		return new Pair<TYPE, Symbol>(ArrayBuilder.build(t, starCount,
				decl.constExpr.size()), id.symbol);
	}

	private Pair<TYPE, Symbol> checkFuncDeclarator(FunDeclarator decl, TYPE t) {
		int starCount = decl.plainDeclarator.starCount;
		Id id = decl.plainDeclarator.id;
		if (decl.para == null)
			return new Pair<TYPE, Symbol>(FunctionBuilder.build(t, starCount,
					null, false, false), id.symbol);
		else {
			List<Pair<TYPE, Symbol>> params = checkParameters(decl.para);
			return new Pair<TYPE, Symbol>(FunctionBuilder.build(t, starCount,
					params, decl.para.varparams, false), id.symbol);
		}
	}

	private TYPE checkConstExpr(ConstExpr constExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	private TYPE checkTypeSpecifier(TypeSpecifier typeSpec) {
		if (typeSpec instanceof VoidType)
			return VOID.getInstance();
		if (typeSpec instanceof CharType)
			return CHAR.getInstance();
		if (typeSpec instanceof IntType)
			return INT.getInstance();
		if (typeSpec instanceof NameType) {
			Object obj = env
					.getByTypedefName(((NameType) typeSpec).typedefName.name);
			if (obj == null)
				error("type undifined");
			else
				return (TYPE) obj;
		}
		if (typeSpec instanceof RecordType) {
			RecordType rType = (RecordType) typeSpec;
			if (rType.id != null && rType.pairs == null) {
				RECORD record = env.getByRecordName(rType.id.symbol);
				if (record != null)
					return record;
				return new NAME(rType.id.symbol);
			} else {
				// TODO
				for (Pair<TypeSpecifier, Declarators> pair : rType.pairs) {
					TYPE type = checkTypeSpecifier(pair.first);
					checkDeclarators(pair.second, type);
				}
			}
		}
		return null;
	}

	private void checkFunctionDefinition(FunctionDefinition funcDecl) {
		// TODO add function definition to env
		TYPE declType = checkTypeSpecifier(funcDecl.typeSpecifier);

		problem;

		env.beginScope();
		checkParameters(funcDecl.parameters);
		TYPE retnType = checkCompStmt(funcDecl.compStmt);
		env.endScope();

		if (!canCast(declType, retnType))
			error("declared type cannot cast from returned type");
	}

	private boolean canCast(TYPE declType, TYPE retnType) {
		// TODO Auto-generated method stub
		if (declType == VOID.getInstance() || retnType == VOID.getInstance())
			return false;
		return true;
	}

	private TYPE checkCompStmt(CompStmt compStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Pair<TYPE, Symbol>> checkParameters(Parameters parameters) {
		ArrayList<Pair<TYPE, Symbol>> params = new ArrayList<Pair<TYPE, Symbol>>();
		HashSet<Symbol> sTable = new HashSet<Symbol>();

		for (PlainDeclaration p : parameters.list) {
			Pair<TYPE, Symbol> pair = checkPlainDeclaration(p);
			if (sTable.contains(pair.second))
				error("arguments of same name in parameters");
			else
				sTable.add(pair.second);
			params.add(pair);
		}
		return params;
	}

	private Pair<TYPE, Symbol> checkPlainDeclaration(PlainDeclaration p) {
		// TODO Auto-generated method stub
		TYPE type = checkTypeSpecifier(p.type);
		return checkDeclarator(p.declarator, type);
	}

}
