package compiler.translate;

import java.util.ArrayList;
import java.util.List;

import compiler.absyn.ArrDeclarator;
import compiler.absyn.AssExpr;
import compiler.absyn.BinOp;
import compiler.absyn.BreakStmt;
import compiler.absyn.CharType;
import compiler.absyn.CompStmt;
import compiler.absyn.ConstExpr;
import compiler.absyn.ContinueStmt;
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
import compiler.absyn.ReturnStmt;
import compiler.absyn.SelStmt;
import compiler.absyn.Stmt;
import compiler.absyn.StructUnion;
import compiler.absyn.TypeSpecifier;
import compiler.absyn.VoidType;
import compiler.builder.FunctionBuilder;
import compiler.env.Env;
import compiler.quad.Binop;
import compiler.quad.Call;
import compiler.quad.Enter;
import compiler.quad.Goto;
import compiler.quad.IfFalse;
import compiler.quad.LabelQuad;
import compiler.quad.Leave;
import compiler.quad.Move;
import compiler.quad.Quad;
import compiler.quad.Return;
import compiler.symbol.Symbol;
import compiler.temp.Addr;
import compiler.temp.AddrList;
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

	public Translate() {
		env = new Env();
	}

	public void emit(Quad q, boolean isTopLevel) {
		if (isTopLevel)
			topLevelQuads.add(q);
		else
			quads.add(q);
	}

	// =====================================
	// builder methods

	private void emitMove(Addr target, Addr source, boolean isTopLevel) {
		if (target instanceof Reference && source instanceof Reference)
			source = loadToTemp((Reference) source, isTopLevel);
		emit(new Move(target, source), isTopLevel);
	}

	private void emitBinop(Temp target, Addr addr1, Addr addr2, BinOp op,
			boolean isTopLevel) {
		if (addr1 instanceof Reference)
			addr1 = loadToTemp((Reference) addr1, isTopLevel);

		if (addr2 instanceof Reference)
			addr2 = loadToTemp((Reference) addr2, isTopLevel);

		emit(new Binop(target, addr1, addr2, op), isTopLevel);
	}

	private void emitCall(Temp target, Addr source, List<Temp> params,
			boolean isTopLevel) {
		emit(new Call(target, source, params), isTopLevel);
	}

	private void emitLabel(Label label) {
		emit(new LabelQuad(label), false);
	}

	private void emitCall(Temp target, Addr source, Temp param,
			boolean isTopLevel) {
		List<Temp> params = new ArrayList<Temp>();
		params.add(param);
		emit(new Call(target, source, params), isTopLevel);
	}

	// =====================================
	// helper methods

	private Temp loadToTemp(Addr r, boolean isTopLevel) {
		Temp temp = new Temp();
		emit(new Move(temp, r), isTopLevel);
		return temp;
	}

	private Temp allocate(Addr totalSize, boolean isTopLevel) {
		if (!(totalSize instanceof Temp))
			totalSize = loadToTemp(totalSize, isTopLevel);
		Addr mAddr = env.getAddr(Symbol.symbol("malloc"));
		Temp ret = new Temp();
		emitCall(ret, mAddr, (Temp) totalSize, isTopLevel);
		return ret;
	}

	private Temp allocate(TYPE type, boolean isTopLevel) {
		Temp temp = null;

		if (type instanceof STRUCT || type instanceof ARRAY)
			temp = allocate(type.size, isTopLevel);

		if (temp == null)
			temp = new Temp();
		return temp;
	}

	private void copyStruct(Temp target, AddrList source, boolean isTopLevel,
			STRUCT type) {

		// XXX might be wrong for char
		Temp ptr = loadToTemp(target, isTopLevel);
		for (int i = 0; i < source.addrs.size(); i++) {
			RecordField rf = type.fields.get(i);
			Addr addr = source.addrs.get(i);
			if (rf.type instanceof STRUCT)
				copyStruct(ptr, (AddrList) addr, isTopLevel, (STRUCT) rf.type);
			else {
				if (rf.type instanceof ARRAY)
					copyArray(ptr, (AddrList) addr, isTopLevel, (ARRAY) rf.type);
				else
					emitMove(new Reference(ptr), addr, isTopLevel);
			}
			emitBinop(ptr, ptr, rf.type.size, BinOp.ADD, isTopLevel);
		}
	}

	private void copyArray(Temp target, AddrList source, boolean isTopLevel,
			ARRAY type) {
		Temp ptr = loadToTemp(target, isTopLevel);
		TYPE eleType = type.elementType;
		for (int i = 0; i < source.addrs.size(); i++) {
			Addr addr = source.addrs.get(i);
			if (eleType instanceof STRUCT)
				copyStruct(ptr, (AddrList) addr, isTopLevel, (STRUCT) eleType);
			else {
				if (eleType instanceof ARRAY)
					copyArray(ptr, (AddrList) addr, isTopLevel, (ARRAY) eleType);
				else
					emitMove(new Reference(ptr), addr, isTopLevel);
			}
			emitBinop(ptr, ptr, eleType.size, BinOp.ADD, isTopLevel);
		}
	}

	// =====================================

	public void transProgram(Program program) {
		for (Node node : program.node) {
			if (node instanceof Declaration)
				transDeclaration((Declaration) node, true);
			if (node instanceof FunctionDefinition)
				transFunctionDefinition((FunctionDefinition) node);
		}
	}

	private void transDeclaration(Declaration decl, boolean isTopLevel) {
		TYPE type = transTypeSpecifier(decl.typeSpecifier, isTopLevel);
		if (decl.declarators != null) {

			List<Pair<TYPE, Symbol>> list = transDeclarators(decl.declarators,
					type);

			for (Pair<TYPE, Symbol> pair : list) {
				env.putIden(pair.first, pair.second);
				env.putAddr(pair.second, allocate(pair.first, isTopLevel));
			}
		} else
			transInitDeclarators(decl.initDeclarators, type, isTopLevel);
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
			Addr addr = transConstExpr(constExpr);

			Temp temp = new Temp();
			emitBinop(temp, addr, last, BinOp.MUL, false);
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
		return transDeclarator(p.declarator, transTypeSpecifier(p.type, false));
	}

	private void transInitDeclarators(InitDeclarators initDeclarators,
			TYPE type, boolean isTopLevel) {
		for (InitDeclarator initd : initDeclarators.initDecl)
			transInitDeclarator(initd, type, isTopLevel);
	}

	private void transInitDeclarator(InitDeclarator initd, TYPE type,
			boolean isTopLevel) {
		Pair<TYPE, Symbol> pair = transDeclarator(initd.declarator, type);
		Temp temp = allocate(pair.first, isTopLevel);

		env.putIden(pair.first, pair.second);
		env.putAddr(pair.second, temp);

		if (initd.initializer == null)
			return;

		Addr addr = transInitializer(initd.initializer, pair.first);

		if (pair.first instanceof ARRAY)
			copyArray(temp, (AddrList) addr, isTopLevel, (ARRAY) pair.first);
		else {
			if (pair.first instanceof STRUCT)
				copyStruct(temp, (AddrList) addr, isTopLevel,
						(STRUCT) pair.first);
			else
				emitMove(temp, addr, isTopLevel);
		}
	}

	private Addr transInitializer(Initializer initializer, TYPE type) {
		if (initializer.assExpr != null)
			return transAssExpr(initializer.assExpr);
		else {
			AddrList addrList = new AddrList();
			for (Initializer init : initializer.initializer) {
				addrList.add(transInitializer(init, type));
			}
			return addrList;
		}
	}

	private TYPE transTypeSpecifier(TypeSpecifier typeSpec, boolean isTopLevel) {

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
				TYPE type = transTypeSpecifier(pair.first, isTopLevel);
				List<Pair<TYPE, Symbol>> pairs = transDeclarators(pair.second,
						type);

				for (Pair<TYPE, Symbol> p : pairs) {
					record.addField(p.first, p.second);
					emitBinop(size, size, p.first.size, BinOp.ADD, isTopLevel);
				}
			}
			record.size = size;
			return record;
		}

		return null;
	}

	private void transFunctionDefinition(FunctionDefinition funcDecl) {
		TYPE declType = transTypeSpecifier(funcDecl.typeSpecifier, false);
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
		emit(new Enter(label, params), false);

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
			emit(new Goto(continueLabel), false);
		if (stmt instanceof BreakStmt)
			emit(new Goto(breakLabel), false);

		if (stmt instanceof ReturnStmt) {
			ReturnStmt r = (ReturnStmt) stmt;
			if (r.expr == null) {
				Addr addr = transExpr(r.expr);
				Temp temp = new Temp();
				// XXX maybe wrong for structure
				emitMove(temp, addr, false);
				emit(new Return(temp), false);
			}
			emit(new Leave(funcLabel), false);
		}
	}

	private void transIterStmt(IterStmt stmt, TYPE type, Label funcLabel) {
		Label iterLabel = new Label(), continueLabel = new Label(), endLabel = new Label();
		if (stmt instanceof ForStmt && ((ForStmt) stmt).begin != null)
			transExpr(((ForStmt) stmt).begin);

		emit(new LabelQuad(iterLabel), false);
		Addr addr = transExpr(stmt.cond);
		Temp temp = loadToTemp(addr, false);
		emit(new IfFalse(temp, endLabel), false);

		transStmt(stmt.stmt, type, funcLabel, endLabel, continueLabel);

		emit(new LabelQuad(continueLabel), false);
		if (stmt instanceof ForStmt && ((ForStmt) stmt).end != null)
			transExpr(((ForStmt) stmt).end);
		emit(new Goto(iterLabel), false);

		emit(new LabelQuad(endLabel), false);
	}

	private void transSelStmt(SelStmt stmt, TYPE type, Label funcLabel,
			Label breakLabel, Label continueLabel) {
		Label elseLabel = new Label();
		Label endLabel = stmt.elseStmt == null ? elseLabel : new Label();
		Addr exprAddr = transExpr(stmt.cond);

		Temp t = new Temp();
		emitMove(t, exprAddr, false);
		emit(new IfFalse(t, elseLabel), false);
		transStmt(stmt.thenStmt, type, funcLabel, breakLabel, continueLabel);

		if (stmt.elseStmt != null)
			emit(new Goto(endLabel), false);
		emit(new LabelQuad(elseLabel), false);

		if (stmt.elseStmt != null) {
			transStmt(stmt.elseStmt, type, funcLabel, breakLabel, continueLabel);
			emit(new LabelQuad(endLabel), false);
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
			transDeclaration(d, false);
		for (Stmt stmt : compStmt.stmt)
			transStmt(stmt, type, funcLabel, breakLabel, continueLabel);
		env.endScope();
	}

	private Addr transExpr(Expr expr) {
		return null;
		// TODO Auto-generated method stub

	}

	private Addr transConstExpr(ConstExpr constExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	private Addr transAssExpr(AssExpr assExpr) {
		// TODO Auto-generated method stub
		return null;
	}

}
