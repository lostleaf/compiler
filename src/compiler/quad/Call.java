package compiler.quad;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Addr;
import compiler.temp.Label;
import compiler.temp.Temp;

public class Call extends Quad {
	Temp dst;
	Addr src;
	List<Temp> params;

	public Call(Temp target, Addr source, List<Temp> params) {
		this.dst = target;
		this.src = source;
		this.params = params;
	}

	public String toString() {
		String s = params != null ? params.toString() : "";
		return dst + " = call " + src + " " + s;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.addAll(params);
		if (src instanceof Temp)
			set.add((Temp) src);
		return set;
	}

	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if (dst != null)
			set.add(dst);
		return set;
	}

	@Override
	public AssemList gen() {
		if (dst == null) {
			if (src instanceof Label)
				return L(saveArguments(), L(new Assem("jal %", src)));
			else
				return L(saveArguments(), L(new Assem("jalr %", src)));
		} else {
			if (src instanceof Label)
				return L(
						saveArguments(),
						L(new Assem("jal %", src), L(new Assem("move @, $v0",
								dst))));
			else
				return L(
						saveArguments(),
						L(new Assem("jalr %", src), L(new Assem("move @, $v0",
								dst))));
		}
	}

	private AssemList saveArguments() {
		AssemList saves = null;
		int i = 0;
		while (i < paramRegNum && i < params.size()) {
			saves = L(saves, L(new Assem("move $%, %", regNames[paramRegBase
					+ i], params.get(i))));
			++i;
		}
		if (i < params.size()) { // param reg is not enough
			while (i < params.size()) {
				saves = L(saves, L(new Assem("sw %, %($v1)", params.get(i), i
						* wordSize)));
				++i;
			}
		}
		return saves;
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
		List<Temp> newParams = new LinkedList<Temp>();
		for (Temp param : params) {
			if (param.equals(old)) {
				newParams.add(t);
			} else {
				newParams.add(param);
			}
		}
		params = newParams;
		if (src.equals(old))
			src = t;
	}
}
