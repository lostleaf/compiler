package compiler.semantic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import compiler.absyn.ArrDeclarator;
import compiler.absyn.AssExpr;
import compiler.absyn.CharType;
import compiler.absyn.CompStmt;
import compiler.absyn.ConstExpr;
import compiler.absyn.Declaration;
import compiler.absyn.Declarator;
import compiler.absyn.Declarators;
import compiler.absyn.Expr;
import compiler.absyn.ExprStmt;
import compiler.absyn.ForStmt;
import compiler.absyn.FunDeclarator;
import compiler.absyn.FunctionDefinition;
import compiler.absyn.Id;
import compiler.absyn.InitDeclarator;
import compiler.absyn.InitDeclarators;
import compiler.absyn.Initializer;
import compiler.absyn.IntType;
import compiler.absyn.IterStmt;
import compiler.absyn.JumpStmt;
import compiler.absyn.NameType;
import compiler.absyn.Node;
import compiler.absyn.Parameters;
import compiler.absyn.PlainDeclaration;
import compiler.absyn.Program;
import compiler.absyn.RecordType;
import compiler.absyn.SelStmt;
import compiler.absyn.Stmt;
import compiler.absyn.StructUnion;
import compiler.absyn.TypeSpecifier;
import compiler.absyn.VoidType;
import compiler.absyn.WhileStmt;
import compiler.builder.ArrayBuilder;
import compiler.builder.FunctionBuilder;
import compiler.env.Env;
import compiler.symbol.Symbol;
import compiler.type.CHAR;
import compiler.type.FUNCTION;
import compiler.type.INT;
import compiler.type.RECORD;
import compiler.type.STRUCT;
import compiler.type.TYPE;
import compiler.type.UNION;
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
		if (decl.declarators != null) {
			List<Pair<TYPE, Symbol>> list = checkDeclarators(decl.declarators,
					type);
			for (Pair<TYPE, Symbol> pair : list)
				env.putIden(pair.first, pair.second);
		} else if (decl.initDeclarators != null)
			checkInitDeclarators(decl.initDeclarators, type);
	}

	private void checkInitDeclarators(InitDeclarators initDeclarators, TYPE type) {
		for (InitDeclarator initd : initDeclarators.initDecl) {
			Pair<TYPE, Symbol> pair = checkInitDeclarator(initd, type);
			env.putIden(pair.first, pair.second);
		}
	}

	private Pair<TYPE, Symbol> checkInitDeclarator(InitDeclarator initd,
			TYPE type) {
		Pair<TYPE, Symbol> pair = checkDeclarator(initd.declarator, type);
		if (initd.initializer != null) {
			TYPE fromType = checkInitializer(initd.initializer, pair.first);
			if (!canCast(fromType, pair.first))
				error("cannnot cast type in initial declaration");
		}
		return pair;
	}

	private TYPE checkInitializer(Initializer initializer, TYPE type) {
		if (initializer.assExpr != null) {
			TYPE fromType = checkAssExpr(initializer.assExpr);
			if (!canCast(fromType, type))
				error("cannot cast type in initializer");
			return type;
		} else {
			// XXX there might be a problem
			Class ty = null;
			TYPE tty = null;
			for (Initializer init : initializer.initializer) {
				tty = checkInitializer(init, type);
				if (ty == null)
					ty = tty.getClass();
				if (!(ty.isInstance(ty)))
					error("initializer should be same type");
			}
			return tty;
		}
	}

	private List<Pair<TYPE, Symbol>> checkDeclarators(Declarators declarators,
			TYPE type) {
		List<Pair<TYPE, Symbol>> list = new ArrayList<Pair<TYPE, Symbol>>();
		for (Declarator decl : declarators.declarator) {
			Pair<TYPE, Symbol> pair = checkDeclarator(decl, type);
			list.add(pair);
		}
		return list;
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
				if (record != null) {
					if (rType.structUnion == StructUnion.STRUCT
							&& record instanceof UNION)
						error("record should be struct but defined as union");
					if (rType.structUnion == StructUnion.UNION
							&& record instanceof STRUCT)
						error("record should be union but defined as struct");
					return record;
				} else
					error("undifined record");
			} else {
				RECORD record = rType.structUnion == StructUnion.STRUCT ? new STRUCT(
						rType.id.symbol) : new UNION(rType.id.symbol);
				for (Pair<TypeSpecifier, Declarators> pair : rType.pairs) {
					TYPE type = checkTypeSpecifier(pair.first);
					List<Pair<TYPE, Symbol>> pairs = checkDeclarators(
							pair.second, type);
					for (Pair<TYPE, Symbol> p : pairs)
						record.addField(p.first, p.second);
				}
				return record;
			}
		}
		return null;
	}

	private void checkFunctionDefinition(FunctionDefinition funcDecl) {
		TYPE declType = checkTypeSpecifier(funcDecl.typeSpecifier);
		env.beginScope();
		ArrayList<Pair<TYPE, Symbol>> list = checkParameters(funcDecl.parameters);
		FUNCTION func = FunctionBuilder.build(declType,
				funcDecl.plainDeclarator.starCount, list,
				funcDecl.parameters.varparams, true);
		env.putFunc(func, funcDecl.plainDeclarator.id.symbol);
		TYPE retnType = checkCompStmt(funcDecl.compStmt);
		env.endScope();

		if (!canCast(declType, retnType))
			error("declared type cannot cast from returned type");
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
		TYPE type = checkTypeSpecifier(p.type);
		return checkDeclarator(p.declarator, type);
	}

	private TYPE checkStmt(Stmt stmt) {
		if (stmt instanceof ExprStmt)
			return checkExprStmt((ExprStmt) stmt);
		if (stmt instanceof CompStmt)
			return checkCompStmt((CompStmt) stmt);
		if (stmt instanceof SelStmt)
			return checkSelStmt((SelStmt) stmt);
		if (stmt instanceof IterStmt)
			return checkIterStmt((IterStmt) stmt);
		if (stmt instanceof JumpStmt)
			return checkJumpStmt((JumpStmt) stmt);
		return null;
	}

	private TYPE checkJumpStmt(JumpStmt stmt) {
		// TODO Auto-generated method stub
		return null;
	}

	private TYPE checkIterStmt(IterStmt stmt) {
		//FIXME no consideration about return
		loopCount++;
		if(stmt instanceof ForStmt)
			checkForStmt((ForStmt) stmt);
		if(stmt instanceof WhileStmt)
			checkWhileStmt((WhileStmt) stmt);
		loopCount--;
		return null;
	}

	private void checkWhileStmt(WhileStmt stmt) {
		// TODO Auto-generated method stub
		
	}

	private void checkForStmt(ForStmt stmt) {
		// TODO Auto-generated method stub
		
	}

	private TYPE checkSelStmt(SelStmt stmt) {
		//FIXME no consideration about return 
		TYPE condType = checkExpr(stmt.cond);
		if (condType == null || condType == VOID.getInstance())
			error("not correct type of condition");
		checkStmt(stmt.thenStmt);
		if (stmt.elseStmt != null)
			checkStmt(stmt.elseStmt);
		return null;
	}

	private TYPE checkExprStmt(ExprStmt stmt) {
		if (stmt.expr == null)
			return null;
		return checkExpr(stmt.expr);
	}

	private TYPE checkCompStmt(CompStmt compStmt) {
		env.beginScope();
		for (Declaration d : compStmt.declaration)
			checkDeclaration(d);
		TYPE retnType = null;
		for (Stmt stmt : compStmt.stmt)
			retnType = checkStmt(stmt);
		env.endScope();
		return retnType;
	}

	private TYPE checkConstExpr(ConstExpr constExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	private TYPE checkExpr(Expr expr) {
		// TODO Auto-generated method stub
		return null;
	}

	private TYPE checkAssExpr(AssExpr assExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean canCast(TYPE fromType, TYPE toType) {
		// TODO Add more information of cast
		if (fromType == VOID.getInstance() || toType == VOID.getInstance())
			return false;
		return true;
	}
}
