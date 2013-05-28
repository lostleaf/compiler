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
	Temp target;
	Addr source;
	List<Temp> params;

	public Call(Temp target, Addr source, List<Temp> params) {
		this.target = target;
		this.source = source;
		this.params = params;
	}

	public String toString() {
		String s1 = params != null ? params.toString() : "";
		String s2 = target != null ? target.toString() : "";
		return s2 + " = call " + source + " " + s1;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.addAll(params);
		if (source instanceof Temp)
			set.add((Temp) source);
		return set;
	}

	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if (target != null)
			set.add(target);
		return set;
	}

	@Override
	public AssemList gen() {
		if (target == null) {
			if (source instanceof Label)
				return L(saveArguments(), L(new Assem("jal %", source)));
			else
				return L(saveArguments(), L(new Assem("jalr %", source)));
		} else {
			if (source instanceof Label)
				return L(
						saveArguments(),
						L(new Assem("jal %", source), L(new Assem("move @, $v0",
								target))));
			else
				return L(
						saveArguments(),
						L(new Assem("jalr %", source), L(new Assem("move @, $v0",
								target))));
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
		if (source.equals(old))
			source = t;
	}
}
