package compiler.translate;

import java.util.ArrayList;
import java.util.List;

import compiler.absyn.AddExpr;
import compiler.absyn.AndExpr;
import compiler.absyn.ArrDeclarator;
import compiler.absyn.ArrPostfix;
import compiler.absyn.AssExpr;
import compiler.absyn.BinExpr;
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
import compiler.absyn.ReturnStmt;
import compiler.absyn.SelStmt;
import compiler.absyn.SelfDecPostfix;
import compiler.absyn.SelfIncPostfix;
import compiler.absyn.Stmt;
import compiler.absyn.StringExpr;
import compiler.absyn.StructUnion;
import compiler.absyn.TypeName;
import compiler.absyn.TypeSpecifier;
import compiler.absyn.UnaryExpr;
import compiler.absyn.UnaryOp;
import compiler.absyn.ValAttrPostfix;
import compiler.absyn.VoidType;
import compiler.builder.FunctionBuilder;
import compiler.env.Env;
import compiler.quad.Binop;
import compiler.quad.Call;
import compiler.quad.Enter;
import compiler.quad.Goto;
import compiler.quad.IfFalse;
import compiler.quad.IfTrue;
import compiler.quad.LabelQuad;
import compiler.quad.Leave;
import compiler.quad.Move;
import compiler.quad.Quad;
import compiler.quad.Return;
import compiler.quad.Unaryop;
import compiler.symbol.Symbol;
import compiler.temp.Addr;
import compiler.temp.AddrList;
import compiler.temp.IntConstant;
import compiler.temp.Label;
import compiler.temp.Reference;
import compiler.temp.Temp;
import compiler.type.ARRAY;
import compiler.type.CHAR;
import compiler.type.FUNCTION;
import compiler.type.INT;
import compiler.type.POINTER;
import compiler.type.RECORD;
import compiler.type.RECORD.RecordField;
import compiler.type.STRUCT;
import compiler.type.TYPE;
import compiler.type.UNION;
import compiler.type.VOID;
import compiler.util.Config;
import compiler.util.Pair;

public class Translate implements Config {
	private Env env;
	// public List<Quad> quads = new ArrayList<Quad>();
	// public List<Quad> topLevelQuads = new ArrayList<Quad>();
	public List<DataFrag> dataFrags = new ArrayList<DataFrag>();
	public boolean isTopLevel = true;
	public int topLevelSize = 0;
	int maxArgc;

	CompilationUnit cu;
	List<CompilationUnit> cus = new ArrayList<CompilationUnit>();

	public Translate() {
		env = new Env();
		cu = new CompilationUnit();
		cus.add(cu);
		maxArgc = 0;
		env.putAddr(Symbol.symbol("printf"), new Label(true));
		env.putAddr(Symbol.symbol("malloc"), new Label(true));
	}

	public void emit(Quad q) {
		cu.addQuad(q);
	}

	// =====================================
	// emit methods

	private void emitMove(Addr target, Addr source) {
		if (target instanceof IntConstant || target instanceof Label) {
			System.err.println("something goes wrong emitting move");
			return;
		}

		if (!(target instanceof Temp))
			source = loadToTemp(source);
		emit(new Move(target, source));
	}

	private void emitBinop(Temp target, Addr addr1, Addr addr2, BinOp op) {
		if (!(addr1 instanceof Temp))
			addr1 = loadToTemp((Reference) addr1);

		if (!(addr2 instanceof Temp))
			addr2 = loadToTemp((Reference) addr2);

		emit(new Binop(target, addr1, addr2, op));
	}

	private void emitLabel(Label label) {
		emit(new LabelQuad(label));
	}

	private void emitCall(Temp target, Addr source, List<Temp> params) {
		maxArgc = Math.max(maxArgc, params.size());
		if (!(source instanceof Temp) && !(source instanceof Label))
			source = loadToTemp(source);
		emit(new Call(target, source, params));
	}

	private void emitCall(Temp target, Addr source, Addr param) {
		Temp temp = param instanceof Temp ? (Temp) param : loadToTemp(param);
		List<Temp> params = new ArrayList<Temp>();
		params.add(temp);
		emitCall(target, source, params);
	}

	// =====================================
	// helper methods
	private Temp newTemp() {
		return cu.newTemp();
	}

	private Temp loadRefToTemp(Addr addr) {
		Temp temp = newTemp();
		if (addr instanceof Temp)
			emitMove(temp, addr);
		if (addr instanceof Reference) {
			Reference ref = (Reference) addr;
			emitBinop(temp, ref.base, ref.offset, BinOp.ADD);
		}
		return temp;
	}

	private Temp loadToTemp(Addr r) {
		Temp temp = newTemp();
		emit(new Move(temp, r));
		return temp;
	}

	private void copyStruct(STRUCT type, Addr l, Addr r) {

		Temp lt = loadRefToTemp(l), rt = loadRefToTemp(r);
		Temp os = loadToTemp(new IntConstant(0));

		Label startLabel = new Label();
		emit(new LabelQuad(startLabel));

		// copy with a loop
		Temp temp = newTemp();
		emitMove(new Reference(rt, os), new Reference(lt, os));
		emitBinop(os, os, new IntConstant(4), BinOp.ADD);
		emitBinop(temp, os, type.size, BinOp.LESS);
		emit(new IfTrue(temp, startLabel));
	}

	private Reference allocate(Addr size) {
		Addr mallocAddr = env.getAddr(Symbol.symbol("malloc"));
		if (isTopLevel) {
			if (size instanceof IntConstant) {
				int s = ((IntConstant) size).value;
				topLevelSize += s;
				return new Reference(Temp.gp, new IntConstant(topLevelSize - s));
			}

			Temp res = newTemp();
			emitCall(res, mallocAddr, size);

			Reference ref;
			ref = new Reference(Temp.gp, new IntConstant(topLevelSize));
			topLevelSize += wordSize;
			emitMove(ref, res);

			return ref;
		}

		Reference ref = cu.level.get();
		if (size instanceof IntConstant) {
			cu.level.size += ((IntConstant) size).value;
			return ref;
		} else {
			cu.level.size += wordSize;

			Temp res = newTemp();
			emitCall(res, mallocAddr, size);
			emitMove(ref, res);

			return ref;
		}
	}

	private void initStruct(Reference temp, AddrList source, STRUCT type) {

		// XXX might be wrong for char
		Temp p = loadRefToTemp(temp);
		for (int i = 0; i < source.addrs.size(); i++) {
			RecordField rf = type.fields.get(i);
			Addr addr = source.addrs.get(i);
			if (rf.type instanceof STRUCT)
				initStruct(new Reference(p), (AddrList) addr, (STRUCT) rf.type);
			else if (rf.type instanceof ARRAY)
				initArray(new Reference(p), (AddrList) addr, (ARRAY) rf.type);
			else
				emitMove(new Reference(p), addr);

			emitBinop(p, p, rf.type.size, BinOp.ADD);
		}
	}

	private void initArray(Reference temp, AddrList source, ARRAY type) {
		Temp ptr = loadToTemp(temp);
		TYPE eleType = type.elementType;
		for (int i = 0; i < source.addrs.size(); i++) {
			Addr addr = source.addrs.get(i);
			if (eleType instanceof STRUCT)
				initStruct(new Reference(ptr), (AddrList) addr,
						(STRUCT) eleType);
			else if (eleType instanceof ARRAY)
				initArray(new Reference(ptr), (AddrList) addr, (ARRAY) eleType);
			else
				emitMove(new Reference(ptr), addr);
			emitBinop(ptr, ptr, eleType.size, BinOp.ADD);
		}
	}

	// =====================================

	public void transProgram(Program program) {
		for (Node node : program.node) {
			isTopLevel = node instanceof Declaration;

			if (node instanceof Declaration)
				transDeclaration((Declaration) node);
			if (node instanceof FunctionDefinition)
				transFunctionDefinition((FunctionDefinition) node);
		}
	}

	private void transDeclaration(Declaration decl) {
		TYPE type = transTypeSpecifier(decl.typeSpecifier);
		if (decl.declarators != null) {

			List<Pair<TYPE, Symbol>> list = transDeclarators(decl.declarators,
					type);

			for (Pair<TYPE, Symbol> pair : list) {
				env.putIden(pair.first, pair.second);
				env.putAddr(pair.second, allocate(pair.first.size));
			}
		} else
			transInitDeclarators(decl.initDeclarators, type);
	}

	private List<Pair<TYPE, Symbol>> transDeclarators(Declarators declarators,
			TYPE type) {
		List<Pair<TYPE, Symbol>> list = new ArrayList<Pair<TYPE, Symbol>>();

		for (Declarator decl : declarators.declarator)
			list.add(transDeclarator(decl, type));

		return list;
	}

	private Pair<TYPE, Symbol> transDeclarator(Declarator decl, TYPE type) {
		if (decl instanceof FunDeclarator)
			return transFuncDeclarator((FunDeclarator) decl, type);
		if (decl instanceof ArrDeclarator)
			return transArrDeclarator((ArrDeclarator) decl, type);
		System.err.println("Declarator is neither Func nor Arr");
		return null;
	}

	private Pair<TYPE, Symbol> transArrDeclarator(ArrDeclarator decl, TYPE type) {
		int starCount = decl.plainDeclarator.starCount;
		Id id = decl.plainDeclarator.id;

		for (int i = 0; i < starCount; i++)
			type = new POINTER(type);

		Addr last = type.size;

		if (last == null)
			System.err.println("a bug happened, last must not be null");

		for (int i = decl.constExpr.size() - 1; i >= 0; i--) {
			ConstExpr constExpr = decl.constExpr.get(i);
			Addr addr = transConstExpr(constExpr).first;

			Temp temp = newTemp();
			emitBinop(temp, addr, last, BinOp.MUL);
			type = new ARRAY(type, addr, temp);

			last = ((ARRAY) type).size;
		}

		return new Pair<TYPE, Symbol>(type, id.symbol);
	}

	private Pair<TYPE, Symbol> transFuncDeclarator(FunDeclarator decl, TYPE t) {
		int starCount = decl.plainDeclarator.starCount;
		Id id = decl.plainDeclarator.id;

		if (decl.para == null)
			return new Pair<TYPE, Symbol>(FunctionBuilder.build(t, starCount,
					null, false, false), id.symbol);
		else {
			List<Pair<TYPE, Symbol>> params = transParameters(decl.para);
			return new Pair<TYPE, Symbol>(FunctionBuilder.build(t, starCount,
					params, decl.para.varparams, false), id.symbol);
		}
	}

	private List<Pair<TYPE, Symbol>> transParameters(Parameters parameters) {
		if (parameters == null)
			return null;

		ArrayList<Pair<TYPE, Symbol>> params = new ArrayList<Pair<TYPE, Symbol>>();

		for (PlainDeclaration p : parameters.list)
			params.add(transPlainDeclaration(p));

		return params;
	}

	private Pair<TYPE, Symbol> transPlainDeclaration(PlainDeclaration p) {
		return transDeclarator(p.declarator, transTypeSpecifier(p.type));
	}

	private void transInitDeclarators(InitDeclarators initDeclarators, TYPE type) {
		for (InitDeclarator initd : initDeclarators.initDecl)
			transInitDeclarator(initd, type, isTopLevel);
	}

	private void transInitDeclarator(InitDeclarator initd, TYPE type,
			boolean isTopLevel) {
		Pair<TYPE, Symbol> pair = transDeclarator(initd.declarator, type);
		Reference temp = allocate(pair.first.size);

		env.putIden(pair.first, pair.second);
		env.putAddr(pair.second, temp);

		if (initd.initializer == null)
			return;

		Addr addr = transInitializer(initd.initializer, pair.first);

		if (pair.first instanceof ARRAY)
			initArray(temp, (AddrList) addr, (ARRAY) pair.first);
		else {
			if (pair.first instanceof STRUCT)
				initStruct(temp, (AddrList) addr, (STRUCT) pair.first);
			else
				emitMove(temp, addr);
		}
	}

	private Addr transInitializer(Initializer initializer, TYPE type) {
		if (initializer.assExpr != null)
			return transAssExpr(initializer.assExpr).first;
		else {
			AddrList addrList = new AddrList();
			for (Initializer init : initializer.initializer) {
				addrList.add(transInitializer(init, type));
			}
			return addrList;
		}
	}

	private TYPE transTypeSpecifier(TypeSpecifier typeSpec) {

		if (typeSpec instanceof VoidType)
			return VOID.getInstance();
		if (typeSpec instanceof CharType)
			return CHAR.getInstance();
		if (typeSpec instanceof IntType)
			return INT.getInstance();

		if (typeSpec instanceof NameType) {
			Object obj = env
					.getByTypedefName(((NameType) typeSpec).typedefName.name);
			return (TYPE) obj;
		}

		if (typeSpec instanceof RecordType) {
			RecordType rType = (RecordType) typeSpec;

			if (rType.id != null && rType.pairs == null)
				return env.getByRecordName(rType.id.symbol);

			// define a record
			Symbol symbol = rType.id == null ? null : rType.id.symbol;
			RECORD record = rType.structUnion == StructUnion.STRUCT ? new STRUCT(
					symbol) : new UNION(symbol);
			env.putType(record, symbol);

			Temp size = loadToTemp(new IntConstant(0));
			for (Pair<TypeSpecifier, Declarators> pair : rType.pairs) {
				TYPE type = transTypeSpecifier(pair.first);
				List<Pair<TYPE, Symbol>> pairs = transDeclarators(pair.second,
						type);

				for (Pair<TYPE, Symbol> p : pairs) {
					Temp t = loadToTemp(size);
					record.addField(p.first, p.second, t);
					emitBinop(size, size, p.first.size, BinOp.ADD);
				}
			}
			record.size = size;
			return record;
		}

		return null;
	}

	private void transFunctionDefinition(FunctionDefinition funcDecl) {
		cu = new CompilationUnit();

		TYPE declType = transTypeSpecifier(funcDecl.typeSpecifier);
		List<Pair<TYPE, Symbol>> paramList = transParameters(funcDecl.parameters);

		boolean varparams = funcDecl.parameters == null ? false
				: funcDecl.parameters.varparams;

		FUNCTION func = FunctionBuilder.build(declType,
				funcDecl.plainDeclarator.starCount, paramList, varparams, true);
		env.putFunc(func, funcDecl.plainDeclarator.id.symbol);

		Label funcLabel = new Label(true);
		emitLabel(funcLabel);
		List<Reference> params = new ArrayList<Reference>();

		env.beginScope();

		if (paramList != null)
			for (Pair<TYPE, Symbol> pair : paramList) {
				env.putIden(pair.first, pair.second);
				Reference ref = allocate(pair.first.size);
				env.putAddr(pair.second, ref);
				params.add(ref);
			}

		emit(new Enter(funcLabel, cu.level, params));

		for (int i = paramRegNum; i < params.size(); ++i)
			emitMove(params.get(i), new Reference(Temp.v1, new IntConstant(i
					* wordSize)));

		transCompStmt(funcDecl.compStmt, declType, funcLabel, null, null);
		env.endScope();

		emit(new Leave(funcLabel, cu.level));

		cus.add(cu);
		cu = cus.get(0);
	}

	private void transStmt(Stmt stmt, TYPE type, Label funcLabel,
			Label breakLabel, Label continueLabel) {
		if (stmt instanceof ExprStmt)
			transExprStmt((ExprStmt) stmt);
		if (stmt instanceof CompStmt)
			transCompStmt((CompStmt) stmt, type, funcLabel, breakLabel,
					continueLabel);
		if (stmt instanceof SelStmt)
			transSelStmt((SelStmt) stmt, type, funcLabel, breakLabel,
					continueLabel);
		if (stmt instanceof IterStmt)
			transIterStmt((IterStmt) stmt, type, funcLabel);
		if (stmt instanceof JumpStmt)
			transJumpStmt((JumpStmt) stmt, type, funcLabel, breakLabel,
					continueLabel);
	}

	private void transJumpStmt(JumpStmt stmt, TYPE type, Label funcLabel,
			Label breakLabel, Label continueLabel) {
		if (stmt instanceof ContinueStmt)
			emit(new Goto(continueLabel));
		if (stmt instanceof BreakStmt)
			emit(new Goto(breakLabel));

		if (stmt instanceof ReturnStmt) {
			ReturnStmt r = (ReturnStmt) stmt;
			if (r.expr == null) {
				Addr addr = transExpr(r.expr).first;
				Temp temp = newTemp();
				// XXX maybe wrong for structure
				emitMove(temp, addr);
				emit(new Return(temp));
			}
			emit(new Leave(funcLabel, cu.level));
		}
	}

	private void transIterStmt(IterStmt stmt, TYPE type, Label funcLabel) {
		Label iterLabel = new Label(), continueLabel = new Label(), endLabel = new Label();
		if (stmt instanceof ForStmt && ((ForStmt) stmt).begin != null)
			transExpr(((ForStmt) stmt).begin);

		emit(new LabelQuad(iterLabel));
		Addr addr = transExpr(stmt.cond).first;
		Temp temp = loadToTemp(addr);
		emit(new IfFalse(temp, endLabel));

		if (stmt.stmt != null)
			transStmt(stmt.stmt, type, funcLabel, endLabel, continueLabel);

		emit(new LabelQuad(continueLabel));
		if (stmt instanceof ForStmt && ((ForStmt) stmt).end != null)
			transExpr(((ForStmt) stmt).end);
		emit(new Goto(iterLabel));

		emit(new LabelQuad(endLabel));
	}

	private void transSelStmt(SelStmt stmt, TYPE type, Label funcLabel,
			Label breakLabel, Label continueLabel) {
		Label elseLabel = new Label();
		Label endLabel = stmt.elseStmt == null ? elseLabel : new Label();
		Addr exprAddr = transExpr(stmt.cond).first;

		Temp t = newTemp();
		emitMove(t, exprAddr);
		emit(new IfFalse(t, elseLabel));
		transStmt(stmt.thenStmt, type, funcLabel, breakLabel, continueLabel);

		if (stmt.elseStmt != null)
			emit(new Goto(endLabel));
		emit(new LabelQuad(elseLabel));

		if (stmt.elseStmt != null) {
			transStmt(stmt.elseStmt, type, funcLabel, breakLabel, continueLabel);
			emit(new LabelQuad(endLabel));
		}
	}

	private void transExprStmt(ExprStmt stmt) {
		if (stmt.expr != null)
			transExpr(stmt.expr);
	}

	private void transCompStmt(CompStmt compStmt, TYPE type, Label funcLabel,
			Label breakLabel, Label continueLabel) {
		env.beginScope();
		for (Declaration d : compStmt.declaration)
			transDeclaration(d);
		for (Stmt stmt : compStmt.stmt)
			transStmt(stmt, type, funcLabel, breakLabel, continueLabel);
		env.endScope();
	}

	private Pair<Addr, TYPE> transExpr(Expr expr) {
		Pair<Addr, TYPE> ret = null;
		for (AssExpr e : expr.assExpr)
			ret = transAssExpr(e);
		return ret;
	}

	private Pair<Addr, TYPE> transConstExpr(ConstExpr constExpr) {
		return constExpr == null ? null : transLogOrExpr(constExpr.logOrExpr);
	}

	private Pair<Addr, TYPE> transAssExpr(AssExpr assExpr) {
		if (assExpr.logOrExpr != null)
			return transLogOrExpr(assExpr.logOrExpr);

		Pair<Addr, TYPE> lExpr = transUnaryExpr(assExpr.unaryExpr);
		Pair<Addr, TYPE> rExpr = transAssExpr(assExpr.assExpr);
		if (lExpr == null || rExpr == null)
			return null;

		if (assExpr.op == BinOp.ASSIGN) {
			if (lExpr.second instanceof STRUCT)
				copyStruct((STRUCT) lExpr.second, lExpr.first, rExpr.first);
			else
				emitMove(lExpr.first, rExpr.first);
		} else {
			Temp temp = newTemp();
			BinOp op = BinOp.values()[assExpr.op.ordinal()
					- (BinOp.ADDASSIGN.ordinal() - BinOp.ADD.ordinal())];

			emitBinop(temp, lExpr.first, rExpr.first, op);
			emitMove(lExpr.first, temp);
		}
		return lExpr;
	}

	private Pair<Addr, TYPE> transLogOrExpr(LogOrExpr logOrExpr) {
		if (logOrExpr.expr.size() == 1)
			return transLogAndExpr(logOrExpr.expr.get(0));

		Label successLabel = new Label(), endLabel = new Label();
		for (LogAndExpr expr : logOrExpr.expr) {
			Pair<Addr, TYPE> pair = transLogAndExpr(expr);
			emit(new IfTrue(pair.first, successLabel));
		}

		Temp temp = newTemp();
		emitMove(temp, new IntConstant(0));
		emit(new Goto(endLabel));

		emit(new LabelQuad(successLabel));
		emitMove(temp, new IntConstant(1));
		emit(new LabelQuad(endLabel));
		return new Pair<Addr, TYPE>(temp, INT.getInstance());
	}

	private Pair<Addr, TYPE> transLogAndExpr(LogAndExpr logAndExpr) {
		if (logAndExpr.expr.size() == 1)
			return transBinExpr(logAndExpr.expr.get(0));

		Label failLabel = new Label(), endLabel = new Label();
		for (InOrExpr expr : logAndExpr.expr) {
			Pair<Addr, TYPE> pair = transBinExpr(expr);
			emit(new IfFalse(pair.first, failLabel));
		}

		Temp temp = newTemp();
		emitMove(temp, new IntConstant(1));
		emit(new Goto(endLabel));

		emit(new LabelQuad(failLabel));
		emitMove(temp, new IntConstant(0));
		emit(new LabelQuad(endLabel));
		return new Pair<Addr, TYPE>(temp, INT.getInstance());
	}

	private Pair<Addr, TYPE> transBinExpr(Expression expr) {
		if (expr instanceof CastExpr)
			return transCastExpr((CastExpr) expr);
		if (expr instanceof AddExpr)
			return transAddExpr((AddExpr) expr);

		BinExpr<?> binExpr = (BinExpr<?>) expr;
		if (binExpr.expr.size() == 1)
			return transBinExpr((Expression) binExpr.expr.get(0));

		Temp old = loadToTemp(transBinExpr((Expression) binExpr.expr.get(0)).first);

		BinOp nullOp = null;
		if (binExpr.op == null) {
			if (binExpr instanceof InOrExpr)
				nullOp = BinOp.OR;
			if (binExpr instanceof ExOrExpr)
				nullOp = BinOp.XOR;
			if (binExpr instanceof AndExpr)
				nullOp = BinOp.AND;
		}

		for (int i = 1; i < binExpr.expr.size(); i++) {
			Expression e = (Expression) binExpr.expr.get(i);
			BinOp op = binExpr.op == null ? nullOp : binExpr.op.get(i - 1);

			Pair<Addr, TYPE> pair = transBinExpr(e);
			if (old == null) {
				old = loadToTemp(pair.first);
				continue;
			}

			Temp temp = newTemp();
			emitBinop(temp, old, pair.first, op);
			old = temp;
		}

		return new Pair<Addr, TYPE>(old, INT.getInstance());
	}

	private Pair<Addr, TYPE> transAddExpr(AddExpr addExpr) {
		Pair<Addr, TYPE> p = transBinExpr(addExpr.expr.get(0));
		Temp old = loadToTemp(p.first);
		TYPE type = p.second;

		for (int i = 1; i < addExpr.expr.size(); i++) {
			BinOp op = addExpr.op.get(i - 1);
			Pair<Addr, TYPE> pair = transBinExpr(addExpr.expr.get(i));
			Addr other = null;

			if (type instanceof POINTER && op == BinOp.ADD) {
				Temp offset = newTemp();
				emitBinop(offset, type.size, pair.first, BinOp.MUL);
				other = offset;
			} else {
				other = pair.first;
				type = INT.getInstance();
			}

			Temp temp = newTemp();
			emitBinop(temp, old, other, BinOp.ADD);
			old = temp;
		}

		return new Pair<Addr, TYPE>(old, type);
	}

	private Pair<Addr, TYPE> transCastExpr(CastExpr castExpr) {
		if (castExpr.expression instanceof UnaryExpr)
			return transUnaryExpr((UnaryExpr) castExpr.expression);

		if (castExpr.expression instanceof CastExpr)
			return new Pair<Addr, TYPE>(
					transCastExpr((CastExpr) castExpr.expression).first,
					transTypeName(castExpr.typeName));
		return null;
	}

	private TYPE transTypeName(TypeName typeName) {
		TYPE t = transTypeSpecifier(typeName.typeSpecifier);
		for (int i = 0; i < typeName.starCount; i++)
			t = new POINTER(t);
		return t;
	}

	private Pair<Addr, TYPE> transUnaryExpr(UnaryExpr unaryExpr) {
		Temp temp = newTemp();
		int et = unaryExpr.exprType;
		if (et == UnaryExpr.POSTEXP)
			return transPostExpr((PostExpr) unaryExpr.expr);

		if (et == UnaryExpr.PREDEC || et == UnaryExpr.PREINC) {
			Pair<Addr, TYPE> pair = transUnaryExpr((UnaryExpr) unaryExpr.expr);
			BinOp op = et == UnaryExpr.PREDEC ? BinOp.SUB : BinOp.ADD;

			emitBinop(temp, pair.first, new IntConstant(1), op);
			return new Pair<Addr, TYPE>(temp, pair.second);
		}

		if (et == UnaryExpr.SIZEUEXP || et == UnaryExpr.SIZETYNAME) {
			TYPE type = et == UnaryExpr.SIZETYNAME ? transTypeName((TypeName) unaryExpr.expr)
					: transUnaryExpr((UnaryExpr) unaryExpr.expr).second;
			emitMove(temp, type.size);
			return new Pair<Addr, TYPE>(temp, INT.getInstance());
		}

		if (et == UnaryExpr.UNARYOP)
			return transUnaryExpr(unaryExpr.op, (CastExpr) unaryExpr.expr);
		return null;
	}

	private Pair<Addr, TYPE> transUnaryExpr(UnaryOp op, CastExpr castExpr) {
		Pair<Addr, TYPE> pair = transCastExpr(castExpr);
		Temp ret = newTemp();
		TYPE type = null;

		if (op == UnaryOp.AND) {
			if (pair.second instanceof ARRAY || pair.second instanceof STRUCT)
				emitMove(ret, pair.first);
			else if (pair.first instanceof Reference) {
				Reference r = (Reference) pair.first;
				emitBinop(ret, r.base, r.offset, BinOp.ADD);
			} else
				System.err.println("error when get address");
			type = new POINTER(pair.second);
		}

		if (op == UnaryOp.PLUS) {
			emitMove(ret, pair.first);
			type = pair.second;
		}

		if (op == UnaryOp.MINUS || op == UnaryOp.NOT || op == UnaryOp.TILDE) {
			Temp temp = loadToTemp(pair.first);
			emit(new Unaryop(ret, op, temp));
			type = pair.second;
		}

		if (op == UnaryOp.STAR) {
			if (pair.first instanceof Temp) {
				Temp temp = loadToTemp(pair.first);
				type = ((POINTER) pair.second).elementType;
				return new Pair<Addr, TYPE>(new Reference(temp), type);
			} else
				System.err.println("unable to handle get value"
						+ pair.first.getClass().toString());
		}

		return new Pair<Addr, TYPE>(ret, type);
	}

	private Pair<Addr, TYPE> transPostExpr(PostExpr expr) {
		Pair<Addr, TYPE> t = transPriExpr(expr.priExpr);
		return transPostfixs(t, expr.postfix);
	}

	private Pair<Addr, TYPE> transPriExpr(PriExpr priExpr) {
		Expression e = priExpr.expr;

		if (e instanceof Id) {
			TYPE t = env.getByIdenName(((Id) e).symbol);
			Addr a = env.getAddr(((Id) e).symbol);
			return new Pair<Addr, TYPE>(a, t);
		}
		// XXX char and string implement may cause problem
		if (e instanceof Constant) {
			if (e instanceof IntConst)
				return new Pair<Addr, TYPE>(new IntConstant(
						((IntConst) e).value), INT.getInstance());
			if (e instanceof CharConst) {
				int c = (int) ((CharConst) e).value.charAt(0);
				return new Pair<Addr, TYPE>(new IntConstant(c),
						CHAR.getInstance());
			}
		}

		if (e instanceof StringExpr) {
			Label l = new Label();
			DataFrag data = new DataFrag(l, ((StringExpr) e).value);
			dataFrags.add(data);
			return new Pair<Addr, TYPE>(l, new ARRAY(CHAR.getInstance(),
					((StringExpr) e).value.length()));
		}

		if (e instanceof Expr) {
			return transExpr((Expr) e);
		}
		return null;
	}

	private Pair<Addr, TYPE> transPostfixs(Pair<Addr, TYPE> t,
			List<Postfix> postfix) {
		for (Postfix p : postfix) {
			if (p instanceof ArrPostfix) {
				Pair<Addr, TYPE> pair = transExpr(((ArrPostfix) p).expression);

				Temp temp = newTemp();
				emitBinop(temp, t.second.size, pair.first, BinOp.MUL);

				t.first = temp;
				t.second = ((POINTER) t.second).elementType;
			}

			if (p instanceof FunPostfix) {
				FunPostfix fun = (FunPostfix) p;
				List<Temp> list = new ArrayList<Temp>();

				if (fun.arguments != null && fun.arguments.assExpr != null)
					for (AssExpr a : fun.arguments.assExpr) {
						Pair<Addr, TYPE> pair = transAssExpr(a);
						Temp temp = pair.first instanceof Temp ? (Temp) pair.first
								: loadToTemp(pair.first);
						list.add(temp);
					}

				Temp temp = newTemp();
				emit(new Call(temp, t.first, list));

				t.first = temp;
				t.second = ((FUNCTION) t.second).returnType;
			}

			if (p instanceof ValAttrPostfix) {
				Symbol s = ((ValAttrPostfix) p).identifier.symbol;
				RecordField field = ((RECORD) t.second).findField(s);
				Temp temp = loadRefToTemp(t.first);

				Temp ptr = newTemp();
				emitBinop(ptr, temp, field.offset, BinOp.ADD);

				t.first = new Reference(ptr);
				t.second = field.type;
			}

			if (p instanceof PtrAttrPostfix) {
				RECORD r = (RECORD) ((POINTER) t.second).elementType;
				Symbol s = ((PtrAttrPostfix) p).identifier.symbol;
				RecordField field = r.findField(s);
				Temp temp = loadRefToTemp(t.first);

				Temp ptr = newTemp();
				emitBinop(ptr, temp, field.offset, BinOp.ADD);

				t.first = new Reference(ptr);
				t.second = field.type;
			}

			if (p instanceof SelfIncPostfix || p instanceof SelfDecPostfix) {
				BinOp op = p instanceof SelfIncPostfix ? BinOp.ADD : BinOp.SUB;
				Temp temp = newTemp();
				emitMove(temp, t.first);
				emitBinop(temp, temp, new IntConstant(1), op);
				t.first = temp;
			}
		}
		return t;
	}
}
