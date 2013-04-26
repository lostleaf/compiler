package compiler.absyn;

import java.util.ArrayList;

public class PostExpr extends Expression {
	public ArrayList<Postfix> postfix;
	public PriExpr priExpr;

	public PostExpr(ArrayList<Postfix> postfix, PriExpr exp) {
		super();
		this.postfix = postfix;
		this.priExpr = exp;
	}

}
