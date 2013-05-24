package compiler.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Temp;

public class Return extends Quad {

	private Temp addr = null;

	public Return(Temp a) {
		addr = a;
	}

	public String toString() {
		return "return " + addr;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add(addr);
		return set;
	}

	@Override
	public AssemList gen() {
		return L(new Assem("move $v0, %", addr));
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
		}
	}
}
