package compiler.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Addr;
import compiler.temp.Temp;

public class Move extends Quad {

	public Addr target = null, source = null;

	public Move(Addr d, Addr s) {
		target = d;
		source = s;
	}

	public Move(Temp d, Temp s, String c) {
		target = d;
		source = s;
		cmt = " #" + c;
	}

	public String toString() {
		return target + " = " + source + cmt;
	}

	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if (target instanceof Temp)
			set.add((Temp) target);
		return set;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if (source instanceof Temp)
			set.add((Temp) source);
		return set;
	}

	@Override
	public AssemList gen() {
		return L(new Assem("move @, %" + cmt, target, source));
	}

	@Override
	public Set<Expression> genExp() {
		return new LinkedHashSet<Expression>();
	}

	@Override
	public Set<Expression> killExp(Set<Expression> U) {
		// FIXME whatif target not instanceof Temp?
		return killExpBy(U, (Temp) target);
	}

	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (source.equals(old)) {
			source = t;
		}
	}
}
