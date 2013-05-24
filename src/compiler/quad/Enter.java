package compiler.quad;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Label;
import compiler.temp.Reference;
import compiler.temp.Temp;
import compiler.translate.Level;

public class Enter extends Quad {

	Label label = null;
	Level level = null;
	List<Reference> params = null;

	public Enter(Label l, Level v, LinkedList<Reference> p) {
		label = l;
		level = v;
		params = p;
	}

	public String toString() {
		return "Enter " + label;
	}

	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		return set;
	}

	@Override
	public AssemList gen() {
		int L = level.frameSize();
		return L(
				new Assem("addiu $sp, $sp, -%", L),
				L(new Assem("sw $ra, %($sp)", L - wordSize),
						L(level.saveRegisters(), loadArguments())));
	}

	private AssemList loadArguments() {
		AssemList loads = null;
		int i = 0;
		while (i < paramRegNum && i < params.size()) {
			Object x = params.get(i).base;
			if (Temp.gp.equals(x))
				x = "$gp";
			if (Temp.sp.equals(x))
				x = "$sp";
			loads = L(loads, L(new Assem("sw $%, %(%)", regNames[paramRegBase
					+ i], params.get(i).offset, x)));
			++i;
		}
		return loads;
	}

	@Override
	public Set<Expression> genExp() {
		return new LinkedHashSet<Expression>();
	}

	@Override
	public Set<Expression> killExp(Set<Expression> U) {
		return U;
	}
}
