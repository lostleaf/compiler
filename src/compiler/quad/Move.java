package compiler.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Addr;
import compiler.temp.IntConstant;
import compiler.temp.Label;
import compiler.temp.Reference;
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

	@Override
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
		if (source instanceof Reference)
			set.add(((Reference) source).base);
		if (target instanceof Reference)
			set.add(((Reference) target).base);
		return set;
	}

	@Override
	public AssemList gen() {
		Object t = target instanceof Temp ? ((Temp)target).getName(): target;
		Object s = source instanceof Temp ? ((Temp)source).getName(): source;

		if (target instanceof Temp) {
			if (source instanceof Temp)
				return L(new Assem("move @, %" + cmt, t, s));
			if (source instanceof Label)
				return L(new Assem("la @, %" + cmt, t, s));
			if (source instanceof IntConstant)
				return L(new Assem("li @, %" + cmt, t, s));
			if (source instanceof Reference) {
				s = ((Reference) source).base.getName();
				return L(new Assem("lw @, %(%)" + cmt, t,
						((Reference) source).offset, s));
			}
		}
		if (target instanceof Reference) {
			if (source instanceof Temp) {
				t = ((Reference) target).base.getName();
				return L(new Assem("sw %, %(%)" + cmt, s,
						((Reference) target).offset, t));
			}
		}
		System.err.println("Move.gen() fails.  " + this);
		return null;
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
		} else if (source instanceof Reference
				&& ((Reference) source).base.equals(old)) {
			((Reference) source).base = t;
		} else if (target instanceof Reference
				&& ((Reference) target).base.equals(old)) {
			((Reference) target).base = t;
		}
	}

	@Override
	public void replaceUseOf(Temp old, IntConstant t) {
		if (target instanceof Temp && source.equals(old)) {
			source = t;
		}
	}
}
