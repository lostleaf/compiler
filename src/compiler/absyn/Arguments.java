package compiler.absyn;

import java.util.List;

public class Arguments extends Node {
	public List<AssExpr> assExpr = null;

	public Arguments(List<AssExpr> list) {
		super();
		this.assExpr = list;
	}

}
