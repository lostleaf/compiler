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

public class Return extends Quad {

	private Addr addr = null;

	public Return(Addr a) {
		addr = a;
	}

	public String toString() {
		return "return " + addr;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if (addr instanceof Temp)
			set.add((Temp) addr);
		return set;
	}

	@Override
	public AssemList gen() {
		if (addr instanceof IntConstant)
			return L(new Assem("li $v0, %", addr));
		if (addr instanceof Label)
			return L(new Assem("la $v0, %", addr));
		Object ad = addr instanceof Temp ? ((Temp) addr).getName() : addr;
		return L(new Assem("move $v0, %", ad));
	}

	@Override
	public Set<Expression> genExp() {
		return new LinkedHashSet<Expression>();
	}

	@Override
	public Set<Expression> killExp(Set<Expression> U) {
		return new LinkedHashSet<Expression>();
	}

	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (addr.equals(old)) {
			addr = t;
		} else if (addr instanceof Reference
				&& ((Reference) addr).base.equals(old)) {
			((Reference) addr).base = t;
		}
	}

	@Override
	public void replaceUseOf(Temp old, IntConstant t) {
		if (addr.equals(old)) {
			addr = t;
		}
	}
}
