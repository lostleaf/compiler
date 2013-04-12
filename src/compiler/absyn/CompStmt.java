package compiler.absyn;

import java.util.List;

public class CompStmt extends Stmt {
	public List<Declaration> declaration;
	public List<Stmt> stmt;

	public CompStmt(List<Declaration> declaration, List<Stmt> stmt) {
		super();
		this.declaration = declaration;
		this.stmt = stmt;
	}

}
