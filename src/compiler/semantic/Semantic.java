package compiler.semantic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import compiler.absyn.AddExpr;
import compiler.absyn.AndExpr;
import compiler.absyn.ArrDeclarator;
import compiler.absyn.ArrPostfix;
import compiler.absyn.AssExpr;
import compiler.absyn.BinOp;
import compiler.absyn.BreakStmt;
import compiler.absyn.CastExpr;
import compiler.absyn.CharConst;
import compiler.absyn.CharType;
import compiler.absyn.CompStmt;
import compiler.absyn.ConstExpr;
import compiler.absyn.Constant;
import compiler.absyn.ContinueStmt;
import compiler.absyn.Declaration;
import compiler.absyn.Declarator;
import compiler.absyn.Declarators;
import compiler.absyn.EquExpr;
import compiler.absyn.ExOrExpr;
import compiler.absyn.Expr;
import compiler.absyn.ExprStmt;
import compiler.absyn.Expression;
import compiler.absyn.ForStmt;
import compiler.absyn.FunDeclarator;
import compiler.absyn.FunPostfix;
import compiler.absyn.FunctionDefinition;
import compiler.absyn.Id;
import compiler.absyn.InOrExpr;
import compiler.absyn.InitDeclarator;
import compiler.absyn.InitDeclarators;
import compiler.absyn.Initializer;
import compiler.absyn.IntConst;
import compiler.absyn.IntType;
import compiler.absyn.IterStmt;
import compiler.absyn.JumpStmt;
import compiler.absyn.LogAndExpr;
import compiler.absyn.LogOrExpr;
import compiler.absyn.MulExpr;
import compiler.absyn.NameType;
import compiler.absyn.Node;
import compiler.absyn.Parameters;
import compiler.absyn.PlainDeclaration;
import compiler.absyn.PostExpr;
import compiler.absyn.Postfix;
import compiler.absyn.PriExpr;
import compiler.absyn.Program;
import compiler.absyn.PtrAttrPostfix;
import compiler.absyn.RecordType;
import compiler.absyn.RelExpr;
import compiler.absyn.ReturnStmt;
import compiler.absyn.SelStmt;
import compiler.absyn.SelfDecPostfix;
import compiler.absyn.SelfIncPostfix;
import compiler.absyn.ShiftExpr;
import compiler.absyn.Stmt;
import compiler.absyn.StringExpr;
import compiler.absyn.StructUnion;
import compiler.absyn.TypeName;
import compiler.absyn.TypeSpecifier;
import compiler.absyn.UnaryExpr;
import compiler.absyn.UnaryOp;
import compiler.absyn.ValAttrPostfix;
import compiler.absyn.VoidType;
import compiler.absyn.WhileStmt;
import compiler.builder.ArrayBuilder;
import compiler.builder.FunctionBuilder;
import compiler.env.Env;
import compiler.symbol.Symbol;
import compiler.type.ARRAY;
import compiler.type.CHAR;
import compiler.type.FUNCTION;
import compiler.type.INT;
import compiler.type.NAME;
import compiler.type.POINTER;
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

	public boolean hasError() {
		return errors.size() > 0;
	}

	public List<Error> getErrors() {
		return errors;
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
			TYPE fromType = checkAssExpr(initializer.assExpr).first;
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
			TYPE type = checkConstExpr(constExpr).first;
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
				// XXX put the struct into env before finish declaration may
				// cause problem
				RECORD record = rType.structUnion == StructUnion.STRUCT ? new STRUCT(
						rType.id.symbol) : new UNION(rType.id.symbol);
				env.putType(record, rType.id.symbol);
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
		boolean varparams = funcDecl.parameters == null ? false
				: funcDecl.parameters.varparams;
		FUNCTION func = FunctionBuilder.build(declType,
				funcDecl.plainDeclarator.starCount, list, varparams, true);
		env.putFunc(func, funcDecl.plainDeclarator.id.symbol);
		TYPE retnType = checkCompStmt(funcDecl.compStmt);
		env.endScope();

		if (!canCast(declType, retnType))
			error("declared type cannot cast from returned type");
	}

	private ArrayList<Pair<TYPE, Symbol>> checkParameters(Parameters parameters) {
		if (parameters == null)
			return null;
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
		if (stmt instanceof ContinueStmt && loopCount == 0)
			error("continue out of loop");
		if (stmt instanceof BreakStmt && loopCount == 0)
			error("break out of loop");
		if (stmt instanceof ReturnStmt) {
			ReturnStmt r = (ReturnStmt) stmt;
			if (r.expr != null)
				return checkExpr(r.expr).first;
		}
		return null;
	}

	private TYPE checkIterStmt(IterStmt stmt) {
		// FIXME no consideration about return
		loopCount++;
		if (stmt instanceof ForStmt)
			checkForStmt((ForStmt) stmt);
		if (stmt instanceof WhileStmt)
			checkWhileStmt((WhileStmt) stmt);
		loopCount--;
		return null;
	}

	private void checkWhileStmt(WhileStmt stmt) {
		TYPE condType = checkExpr(stmt.cond).first;
		if (condType == null || condType == VOID.getInstance())
			error("wrong type condition of while");
		checkStmt(stmt.stmt);
	}

	private void checkForStmt(ForStmt stmt) {
		if (stmt.begin != null)
			checkExpr(stmt.begin);
		if (stmt.cond != null) {
			TYPE condType = checkExpr(stmt.cond).first;
			if (condType == null || condType == VOID.getInstance())
				error("wrong type condition of while");
		}
		if (stmt.end != null)
			checkExpr(stmt.end);
		checkStmt(stmt.stmt);
	}

	private TYPE checkSelStmt(SelStmt stmt) {
		// FIXME no consideration about return
		TYPE condType = checkExpr(stmt.cond).first;
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
		return checkExpr(stmt.expr).first;
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

	private Pair<TYPE, Boolean> checkConstExpr(ConstExpr constExpr) {
		return checkLogOrExpr(constExpr.logOrExpr);
	}

	private Pair<TYPE, Boolean> checkExpr(Expr expr) {
		Pair<TYPE, Boolean> ret = null;
		for (AssExpr e : expr.assExpr)
			ret = checkAssExpr(e);
		return ret;
	}

	private Pair<TYPE, Boolean> checkAssExpr(AssExpr assExpr) {
		// FIXME may have to check is left value
		if (assExpr.logOrExpr != null)
			return checkLogOrExpr(assExpr.logOrExpr);
		else {
			Pair<TYPE, Boolean> lType = checkUnaryExpr(assExpr.unaryExpr);
			Pair<TYPE, Boolean> rType = checkAssExpr(assExpr.assExpr);
			if (lType == null || rType == null)
				return null;

			if (!lType.second)
				error("left part of assignment must be left value");
			if (assExpr.op == BinOp.ASSIGN) {
				if (lType.first instanceof POINTER
						&& rType.first instanceof POINTER)
					if (!((POINTER) lType.first).equals((POINTER) rType.first)) {
						error("A pointer can only be assigned to a pointer");
						return null;
					} else {
						return new Pair<TYPE, Boolean>(lType.first, false);
					}
				if (!(lType.first.equals(rType.first) || (!(lType.first instanceof RECORD || lType.first instanceof POINTER) && !(rType.first instanceof RECORD)))) {
					error("assignment with wrong operands3");
					return null;
				}
				return new Pair<TYPE, Boolean>(lType.first, false);
			} else {
				if ((lType.first instanceof POINTER || lType.first instanceof FUNCTION)
						&& (rType.first instanceof POINTER || rType.first instanceof FUNCTION)) {
					error("assignment with wrong operands");
					return null;
				}
				if (!(lType.first instanceof RECORD)
						&& (rType.first instanceof INT || rType.first instanceof CHAR)) {
					return new Pair<TYPE, Boolean>(lType.first, false);
				}
				if (lType.first.equals(rType.first)) {
					return new Pair<TYPE, Boolean>(lType.first, false);
				}
				error("assignment with wrong operands");
				return null;
			}

		}
	}

	private Pair<TYPE, Boolean> checkLogOrExpr(LogOrExpr logOrExpr) {
		if (logOrExpr.expr.size() == 1)
			return checkLogAndExpr(logOrExpr.expr.get(0));
		for (LogAndExpr e : logOrExpr.expr) {
			TYPE t = checkLogAndExpr(e).first;
			if (t == null)
				return null;
			if (t instanceof RECORD)
				error("record in logical or");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkLogAndExpr(LogAndExpr logAndExpr) {
		if (logAndExpr.expr.size() == 1)
			return checkInOrExpr(logAndExpr.expr.get(0));
		for (InOrExpr e : logAndExpr.expr) {
			TYPE t = checkInOrExpr(e).first;
			if (t == null)
				return null;
			if (t instanceof RECORD)
				error("record in logical and");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkInOrExpr(InOrExpr inOrExpr) {
		if (inOrExpr.expr.size() == 0)
			return checkExOrExpr(inOrExpr.expr.get(0));
		for (ExOrExpr e : inOrExpr.expr) {
			TYPE t = checkExOrExpr(e).first;
			if (t == null)
				return null;
			if (t instanceof RECORD || t instanceof POINTER
					|| t instanceof FUNCTION)
				error("inclusiveOr operator with unmatching operands");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkExOrExpr(ExOrExpr exOrExpr) {
		if (exOrExpr.expr.size() == 1)
			return checkAndExpr(exOrExpr.expr.get(0));
		for (AndExpr e : exOrExpr.expr) {
			TYPE t = checkAndExpr(e).first;
			if (t == null)
				return null;
			if (t instanceof RECORD || t instanceof POINTER
					|| t instanceof FUNCTION)
				error("exclusiveOr operator with unmatching operands");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkAndExpr(AndExpr andExpr) {
		if (andExpr.expr.size() == 1)
			return checkEquExpr(andExpr.expr.get(0));
		for (EquExpr e : andExpr.expr) {
			TYPE t = checkEquExpr(e).first;
			if (t == null)
				return null;
			if (t instanceof RECORD || t instanceof POINTER
					|| t instanceof FUNCTION)
				error("and operator with unmatching operands");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkEquExpr(EquExpr equExpr) {
		if (equExpr.expr.size() == 1)
			return checkRelExpr(equExpr.expr.get(0));
		for (RelExpr e : equExpr.expr) {
			TYPE t = checkRelExpr(e).first;
			if (t == null)
				return null;
			if (t instanceof RECORD || t instanceof NAME)
				error("equality operator with unmatching operands");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkRelExpr(RelExpr relExpr) {
		if (relExpr.expr.size() == 1)
			return checkShiftExpr(relExpr.expr.get(0));
		for (ShiftExpr e : relExpr.expr) {
			TYPE t = checkShiftExpr(e).first;
			if (t == null)
				return null;
			if (t instanceof RECORD || t instanceof NAME)
				error("relation operator with unmatching operands");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkShiftExpr(ShiftExpr shiftExpr) {
		if (shiftExpr.expr.size() == 1)
			return checkAddExpr(shiftExpr.expr.get(0));
		for (AddExpr e : shiftExpr.expr) {
			TYPE t = checkAddExpr(e).first;
			if (t == null)
				return null;
			if (!(t instanceof INT) && !(t instanceof CHAR))
				error("shift operator with unmatching operands");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkAddExpr(AddExpr addExpr) {
		if (addExpr.expr.size() == 1)
			return checkMulExpr(addExpr.expr.get(0));

		TYPE retnType = null;

		for (int i = 0; i < addExpr.expr.size(); i++) {
			MulExpr m = addExpr.expr.get(i);
			TYPE t = checkMulExpr(m).first;
			if (retnType == null) {
				retnType = t;
				continue;
			}
			if ((retnType instanceof INT || retnType instanceof CHAR)
					&& (t instanceof INT || t instanceof CHAR)) {
				retnType = INT.getInstance();
				continue;
			}
			if ((retnType instanceof POINTER || (retnType instanceof FUNCTION && ((FUNCTION) retnType).defined))
					&& (t instanceof INT || t instanceof CHAR))
				continue;
			if (addExpr.op.get(i) == BinOp.ADD
					&& (t instanceof POINTER || (t instanceof FUNCTION && ((FUNCTION) t).defined))
					&& (retnType instanceof INT || retnType instanceof CHAR)) {
				retnType = t;
				continue;
			}
			error("add operator with unmatching operands");
		}
		return new Pair<TYPE, Boolean>(retnType, false);
	}

	private Pair<TYPE, Boolean> checkMulExpr(MulExpr mulExpr) {
		if (mulExpr.expr.size() == 1)
			return checkCastExpr(mulExpr.expr.get(0));
		for (CastExpr e : mulExpr.expr) {
			TYPE t = checkCastExpr(e).first;
			if (t == null)
				return null;
			if (!(t instanceof INT) && !(t instanceof CHAR))
				error("mul operator with unmatching operands");
		}
		return new Pair<TYPE, Boolean>(INT.getInstance(), false);
	}

	private Pair<TYPE, Boolean> checkCastExpr(CastExpr castExpr) {
		if (castExpr.expression instanceof UnaryExpr)
			return checkUnaryExpr((UnaryExpr) castExpr.expression);

		Pair<TYPE, Boolean> pair = checkCastExpr((CastExpr) castExpr.expression);
		return new Pair<TYPE, Boolean>(checkTypeName(castExpr.typeName),
				pair.second);
	}

	private Pair<TYPE, Boolean> checkUnaryExpr(UnaryOp op, Pair<TYPE, Boolean> t) {
		if (t == null)
			return null;

		if (op == UnaryOp.AND) {
			if (!t.second)
				error("the unary and need a left value");
			return new Pair<TYPE, Boolean>(new POINTER(t.first), false);
		}

		if (op == UnaryOp.STAR) {
			if (t.first instanceof POINTER) {
				if (((POINTER) t.first).elementType instanceof VOID)
					error("get value form a void pointer");
				else
					return new Pair<TYPE, Boolean>(
							((POINTER) t.first).elementType, true);
			} else
				error("a pointer needed in unary expression");
		}

		if (op == UnaryOp.AND || op == UnaryOp.MINUS || op == UnaryOp.TILDE) {
			if (!(t.first instanceof INT) && !(t.first instanceof CHAR))
				error("unary plus or minus or tilde before neither a int nor char");
			else
				return new Pair<TYPE, Boolean>(t.first, false);
		}

		if (op == UnaryOp.NOT) {
			if (t.first instanceof FUNCTION)
				error("unary not before a function type");
			else
				return new Pair<TYPE, Boolean>(INT.getInstance(), false);
		}
		return null;

	}

	private Pair<TYPE, Boolean> checkUnaryExpr(UnaryExpr unaryExpr) {
		if (unaryExpr.exprType == UnaryExpr.POSTEXP)
			return checkPostExpr((PostExpr) unaryExpr.expr);

		if (unaryExpr.exprType == UnaryExpr.PREDEC
				|| unaryExpr.exprType == UnaryExpr.PREINC) {
			Pair<TYPE, Boolean> pair = checkUnaryExpr((UnaryExpr) unaryExpr.expr);
			if (!pair.second)
				error("prefix inc/dec need a left value");
			return new Pair<TYPE, Boolean>(pair.first, true);
		}

		if (unaryExpr.exprType == UnaryExpr.UNARYOP) {
			CastExpr c = (CastExpr) unaryExpr.expr;
			Pair<TYPE, Boolean> t = checkCastExpr(c);
			return checkUnaryExpr(unaryExpr.op, t);
		}

		if (unaryExpr.exprType == UnaryExpr.SIZETYNAME
				|| unaryExpr.exprType == UnaryExpr.SIZEUEXP)
			return new Pair<TYPE, Boolean>(INT.getInstance(), false);
		return null;
	}

	private Pair<TYPE, Boolean> checkPostExpr(PostExpr expr) {
		Pair<TYPE, Boolean> t = checkPriExpr(expr.priExpr);
		return checkPostfixs(t, expr.postfix);
	}

	private Pair<TYPE, Boolean> checkPostfixs(Pair<TYPE, Boolean> t,
			List<Postfix> list) {
		for (Postfix p : list) {
			if (p instanceof ArrPostfix)
				t = checkArrPostfix(t.first, (ArrPostfix) p);
			if (p instanceof FunPostfix)
				t = checkFunPostfix(t.first, (FunPostfix) p);
			if (p instanceof ValAttrPostfix)
				t = checkValAttrPostfix(t.first, (ValAttrPostfix) p);
			if (p instanceof PtrAttrPostfix)
				t = checkPtrAttrPostfix(t.first, (PtrAttrPostfix) p);
			if (p instanceof SelfIncPostfix || p instanceof SelfDecPostfix) {
				if (t.first instanceof FUNCTION || t.first instanceof RECORD)
					error("self inc or dec with wrong type");
				if (!t.second)
					error("self inc/dec postfix need a left value");
				t.second = true;
			}
		}
		return t;
	}

	private Pair<TYPE, Boolean> checkPtrAttrPostfix(TYPE t, PtrAttrPostfix p) {
		if (t == null || p.identifier == null)
			return null;
		if (t instanceof POINTER) {
			t = ((POINTER) t).elementType;
			if (t instanceof RECORD) {
				RECORD r = (RECORD) t;
				for (RECORD.RecordField field : r.fields)
					if (field.name.equals(p.identifier.symbol))
						return new Pair<TYPE, Boolean>(field.type, true);
				error("field missing");
			} else
				error("-> need pointer to a record");

		} else
			error("-> to not a pointer");
		return null;
	}

	private Pair<TYPE, Boolean> checkValAttrPostfix(TYPE t, ValAttrPostfix p) {
		if (t == null || p.identifier == null)
			return null;

		if (t instanceof RECORD) {
			RECORD r = (RECORD) t;
			for (RECORD.RecordField field : r.fields)
				if (field.name.equals(p.identifier.symbol))
					return new Pair<TYPE, Boolean>(field.type, true);
			error("field missing");
		} else
			error(". need  a record");

		return null;
	}

	private Pair<TYPE, Boolean> checkFunPostfix(TYPE t, FunPostfix p) {
		if (t == null)
			return null;
		if (!(t instanceof FUNCTION))
			error("function postfix need a function");
		else {
			if (isParaMatch((FUNCTION) t, p.arguments.assExpr))
				return new Pair<TYPE, Boolean>(
						((FUNCTION) t).getFinalReturnType(), false);
			else
				error("params unmatch");
		}
		return null;
	}

	private boolean isParaMatch(FUNCTION t, List<AssExpr> assExpr) {
		// TODO Auto-generated method stub
		return true;
	}

	private Pair<TYPE, Boolean> checkArrPostfix(TYPE t, ArrPostfix p) {
		if (t == null)
			return null;
		if (!(t instanceof ARRAY))
			error("ArrPostfix need an array");
		else {
			Pair<TYPE, Boolean> type = checkExpr((Expr) p.expression);
			if (!(type.first instanceof INT) && !(type.first instanceof CHAR))
				error("wrong type of array index");
			return new Pair<TYPE, Boolean>(((ARRAY) t).elementType, true);
		}
		return null;
	}

	private Pair<TYPE, Boolean> checkPriExpr(PriExpr priExpr) {
		Expression e = priExpr.exp;
		if (e instanceof Id) {
			TYPE t = env.getByIdenName(((Id) e).symbol);
			if (t == null)
				error("undefined identifier");
			if (t instanceof FUNCTION || t instanceof ARRAY)
				return new Pair<TYPE, Boolean>(t, false);
			return new Pair<TYPE, Boolean>(t, true);
		}
		if (e instanceof Constant) {
			if (e instanceof IntConst)
				return new Pair<TYPE, Boolean>(INT.getInstance(), false);
			if (e instanceof CharConst)
				return new Pair<TYPE, Boolean>(CHAR.getInstance(), false);
		}
		if (e instanceof StringExpr) {
			return new Pair<TYPE, Boolean>(new ARRAY(CHAR.getInstance(),
					((StringExpr) e).value.length()), false);
		}
		if (e instanceof Expr) {
			return checkExpr((Expr) e);
		}
		return null;
	}

	private TYPE checkTypeName(TypeName typeName) {
		TYPE t = checkTypeSpecifier(typeName.typeSpecifier);
		for (int i = 0; i < typeName.starCount; i++)
			t = new POINTER(t);
		return t;
	}

	private boolean canCast(TYPE fromType, TYPE toType) {
		// TODO Add more information of cast
		if (fromType == VOID.getInstance() || toType == VOID.getInstance())
			return false;
		return true;
	}
}
