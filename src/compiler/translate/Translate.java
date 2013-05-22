package compiler.translate;

import java.util.ArrayList;
import java.util.List;

import compiler.absyn.AddExpr;
import compiler.absyn.AndExpr;
import compiler.absyn.ArrDeclarator;
import compiler.absyn.AssExpr;
import compiler.absyn.BinExpr;
import compiler.absyn.BinOp;
import compiler.absyn.BreakStmt;
import compiler.absyn.CastExpr;
import compiler.absyn.CharType;
import compiler.absyn.CompStmt;
import compiler.absyn.ConstExpr;
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
import compiler.absyn.FunctionDefinition;
import compiler.absyn.Id;
import compiler.absyn.InOrExpr;
import compiler.absyn.InitDeclarator;
import compiler.absyn.InitDeclarators;
import compiler.absyn.Initializer;
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
import compiler.absyn.Program;
import compiler.absyn.RecordType;
import compiler.absyn.ReturnStmt;
import compiler.absyn.SelStmt;
import compiler.absyn.Stmt;
import compiler.absyn.StructUnion;
import compiler.absyn.TypeName;
import compiler.absyn.TypeSpecifier;
import compiler.absyn.UnaryExpr;
import compiler.absyn.UnaryOp;
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
import compiler.util.Pair;

public class Translate {
	private Env env = null;
	private List<Quad> quads = new ArrayList<Quad>();
	private List<Quad> topLevelQuads = new ArrayList<Quad>();
	private boolean isTopLevel = true;

	public Translate() {
		env = new Env();
	}

	public void emit(Quad q) {
		if (isTopLevel)
			topLevelQuads.add(q);
		else
			quads.add(q);
	}

	// =====================================
	// emit methods

	private void emitMove(Addr target, Addr source) {
		if (target instanceof Reference && source instanceof Reference)
			source = loadToTemp((Reference) source);
		emit(new Move(target, source));
	}

	private void emitBinop(Temp target, Addr addr1, Addr addr2, BinOp op) {
		if (addr1 instanceof Reference)
			addr1 = loadToTemp((Reference) addr1);

		if (addr2 instanceof Reference)
			addr2 = loadToTemp((Reference) addr2);

		emit(new Binop(target, addr1, addr2, op));
	}

	private void emitLabel(Label label) {
		emit(new LabelQuad(label));
	}

	private void emitCall(Temp target, Addr source, Temp param,
			boolean isTopLevel) {
		List<Temp> params = new ArrayList<Temp>();
		params.add(param);
		emit(new Call(target, source, params));
	}

	// =====================================
	// helper methods

	private Temp loadRefToTemp(Addr addr) {
		Temp temp = new Temp();
		if (addr instanceof Temp)
			emitMove(temp, addr);
		else
			emitBinop(temp, ((Reference) addr).base, ((Reference) addr).offset,
					BinOp.ADD);
		return temp;
	}

	private void copyStruct(STRUCT type, Addr l, Addr r) {

		Temp lt = loadRefToTemp(l), rt = loadRefToTemp(r);
		Temp os = loadToTemp(new IntConstant(0));

		Label startLabel = new Label();
		emit(new LabelQuad(startLabel));

		// copy with a loop
		Temp temp = new Temp();
		emitMove(new Reference(rt, os), new Reference(lt, os));
		emitBinop(os, os, new IntConstant(4), BinOp.ADD);
		emitBinop(temp, os, type.size, BinOp.LESS);
		emit(new IfTrue(temp, startLabel));
	}

	private Temp loadToTemp(Addr r) {
		Temp temp = new Temp();
		emit(new Move(temp, r));
		return temp;
	}

	private Temp allocate(Addr totalSize) {
		if (!(totalSize instanceof Temp))
			totalSize = loadToTemp(totalSize);
		Addr mAddr = env.getAddr(Symbol.symbol("malloc"));
		Temp ret = new Temp();
		emitCall(ret, mAddr, (Temp) totalSize, isTopLevel);
		return ret;
	}

	private Temp allocate(TYPE type) {
		Temp temp = null;

		if (type instanceof STRUCT || type instanceof ARRAY)
			temp = allocate(type.size);

		if (temp == null)
			temp = new Temp();
		return temp;
	}

	private void initStruct(Temp target, AddrList source, STRUCT type) {

		// XXX might be wrong for char
		Temp ptr = loadToTemp(target);
		for (int i = 0; i < source.addrs.size(); i++) {
			RecordField rf = type.fields.get(i);
			Addr addr = source.addrs.get(i);
			if (rf.type instanceof STRUCT)
				initStruct(ptr, (AddrList) addr, (STRUCT) rf.type);
			else {
				if (rf.type instanceof ARRAY)
					initArray(ptr, (AddrList) addr, (ARRAY) rf.type);
				else
					emitMove(new Reference(ptr), addr);
			}
			emitBinop(ptr, ptr, rf.type.size, BinOp.ADD);
		}
	}

	private void initArray(Temp target, AddrList source, ARRAY type) {
		Temp ptr = loadToTemp(target);
		TYPE eleType = type.elementType;
		for (int i = 0; i < source.addrs.size(); i++) {
			Addr addr = source.addrs.get(i);
			if (eleType instanceof STRUCT)
				initStruct(ptr, (AddrList) addr, (STRUCT) eleType);
			else {
				if (eleType instanceof ARRAY)
					initArray(ptr, (AddrList) addr, (ARRAY) eleType);
				else
					emitMove(new Reference(ptr), addr);
			}
			emitBinop(ptr, ptr, eleType.size, BinOp.ADD);
		}
	}

	// =====================================

	public void transProgram(Program program) {
		isTopLevel = true;
		for (Node node : program.node) {
			if (node instanceof Declaration)
				transDeclaration((Declaration) node);
			if (node instanceof FunctionDefinition) {
				isTopLevel = false;
				transFunctionDefinition((FunctionDefinition) node);
				isTopLevel = true;
			}
		}
	}

	private void transDeclaration(Declaration decl) {
		TYPE type = transTypeSpecifier(decl.typeSpecifier);
		if (decl.declarators != null) {

			List<Pair<TYPE, Symbol>> list = transDeclarators(decl.declarators,
					type);

			for (Pair<TYPE, Symbol> pair : list) {
				env.putIden(pair.first, pair.second);
				env.putAddr(pair.second, allocate(pair.first));
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

			Temp temp = new Temp();
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
		Temp temp = allocate(pair.first);

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

			Temp size = new Temp();
			for (Pair<TypeSpecifier, Declarators> pair : rType.pairs) {
				TYPE type = transTypeSpecifier(pair.first);
				List<Pair<TYPE, Symbol>> pairs = transDeclarators(pair.second,
						type);

				for (Pair<TYPE, Symbol> p : pairs) {
					record.addField(p.first, p.second);
					emitBinop(size, size, p.first.size, BinOp.ADD);
				}
			}
			record.size = size;
			return record;
		}

		return null;
	}

	private void transFunctionDefinition(FunctionDefinition funcDecl) {
		TYPE declType = transTypeSpecifier(funcDecl.typeSpecifier);
		List<Pair<TYPE, Symbol>> list = transParameters(funcDecl.parameters);
		boolean varparams = funcDecl.parameters == null ? false
				: funcDecl.parameters.varparams;

		FUNCTION func = FunctionBuilder.build(declType,
				funcDecl.plainDeclarator.starCount, list, varparams, true);
		env.putFunc(func, funcDecl.plainDeclarator.id.symbol);

		Label label = new Label();
		emitLabel(label);
		ArrayList<Temp> params = new ArrayList<Temp>();

		env.beginScope();
		if (list != null)
			for (Pair<TYPE, Symbol> pair : list) {
				env.putIden(pair.first, pair.second);
				Temp temp = new Temp();// XXX check function with arguments
				env.putAddr(pair.second, temp);
				params.add(temp);
			}
		emit(new Enter(label, params));

		transCompStmt(funcDecl.compStmt, declType, label, null, null);
		env.endScope();
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
				Temp temp = new Temp();
				// XXX maybe wrong for structure
				emitMove(temp, addr);
				emit(new Return(temp));
			}
			emit(new Leave(funcLabel));
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

		Temp t = new Temp();
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
			Temp temp = new Temp();
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

		Temp temp = new Temp();
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

		Temp temp = new Temp();
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

			Temp temp = new Temp();
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
				Temp offset = new Temp();
				emitBinop(offset, type.size, pair.first, BinOp.MUL);
				other = offset;
			} else {
				other = pair.first;
				type = INT.getInstance();
			}

			Temp temp = new Temp();
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
		Temp temp = new Temp();
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
		// TODO Auto-generated method stub
		return null;
	}

	private Pair<Addr, TYPE> transPostExpr(PostExpr expr) {
		// TODO Auto-generated method stub
		return null;
	}

}
