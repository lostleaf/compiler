package compiler.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import compiler.absyn.BinOp;
import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Addr;
import compiler.temp.Temp;
import compiler.util.Config;

public class Binop extends Quad implements Config {

	public Temp target;
	public Addr left, right;
	public BinOp op;

	public Binop(Temp target, Addr left, Addr right, BinOp op) {
		this.target = target;
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public String toString() {
		return target + " = " + left + " " + bOpStr[op.ordinal()] + " " + right;
	}

	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add(target);
		return set;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if (left instanceof Temp)
			set.add((Temp) left);
		if (right instanceof Temp)
			set.add((Temp) right);
		return set;
	}

	@Override
	public AssemList gen() {
		return L(new Assem("% @, %, %"
				+ (cmt.length() > 0 ? (" # " + cmt) : ""),
				bOpAsm[op.ordinal()], target, left, right));
	}

	public String toExp() {
		return "(" + left + ")" + bOpStr[op.ordinal()] + "(" + right + ")";
	}

	@Override
	public Set<Expression> genExp() {
		Set<Expression> x = new LinkedHashSet<Expression>();
		x.add(new Expression(toExp(), target));
		return x;
	}

	@Override
	public Set<Expression> killExp(Set<Expression> U) {
		return killExpBy(U, target);
	}

	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (left.equals(old)) {
			left = t;
		}
		if (right.equals(old)) {
			right = t;
		}
	}
}