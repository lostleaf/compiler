package compiler.absyn;

import java.util.List;

public class ArrDeclarator extends Declarator {
	public List<ConstExpr> constExpr;

	public ArrDeclarator(PlainDeclarator plainDeclarator, List<ConstExpr> expr) {
		super(plainDeclarator);
		this.constExpr = expr;
	}
}
