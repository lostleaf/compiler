package compiler.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import compiler.absyn.UnaryOp;
import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Addr;
import compiler.temp.IntConstant;
import compiler.temp.Temp;
import compiler.util.Config;

public class Unaryop extends Quad implements Config {
	public Temp dest;
	public Addr left;
	public UnaryOp op;

	public Unaryop(Temp dest, UnaryOp op, Temp left) {
		this.dest = dest;
		this.op = op;
		this.left = left;
	}

	public String toString() {
		return dest + " = " + uOpStr[op.ordinal()] + " " + left;
	}

	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add(dest);
		return set;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if (left instanceof Temp)
			set.add((Temp) left);
		return set;
	}

	@Override
	public AssemList gen() {
		Object l = left, d = dest;
		if (Temp.gp.equals(l))
			l = "$gp";
		if (Temp.gp.equals(d))
			d = "$gp";
		if (Temp.sp.equals(l))
			l = "$sp";
		if (Temp.sp.equals(d))
			d = "$sp";
		if (Temp.v1.equals(l))
			l = "$v1";
		if (Temp.v1.equals(d))
			d = "$v1";

		if (op == UnaryOp.NOT)
			return L(new Assem("seq @, %, $zero", d, l));
		String cmd = op == UnaryOp.MINUS ? "neg" : "not";
		return L(new Assem("% @, %", cmd, d, l));
	}

	public String toExp() {
		return uOpStr[op.ordinal()] + "(" + left + ")";// XXX
	}

	@Override
	public Set<Expression> genExp() {
		Set<Expression> x = new LinkedHashSet<Expression>();
		x.add(new Expression(toExp(), dest));
		return x;
	}

	@Override
	public Set<Expression> killExp(Set<Expression> U) {
		return killExpBy(U, dest);
	}

	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (left.equals(old)) {
			left = t;
		}
	}

	@Override
	public void replaceUseOf(Temp old, IntConstant t) {
		if (left.equals(old)) {
			left = t;
		}
	}

}
