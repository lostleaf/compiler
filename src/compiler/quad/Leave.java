package compiler.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Label;
import compiler.translate.Level;

public class Leave extends Quad {


	Label label = null;
	Level level;

	public Leave(Label l, Level lvl) {
		label = l;
		level = lvl;
	}

	public String toString() {
		return "Leave " + label;
	}

	@Override
	public AssemList gen() {
		int L = level.frameSize();
		return L(
				level.loadRegisters(),
				L(new Assem("lw $ra, %($sp)", L - wordSize),
						L(new Assem("addiu $sp, $sp, %", L), L(new Assem(
								"jr $ra")))));
	}

	@Override
	public Set<Expression> genExp() {
		return new LinkedHashSet<Expression>();
	}

	@Override
	public Set<Expression> killExp(Set<Expression> U) {
		return new LinkedHashSet<Expression>();
	}

}
