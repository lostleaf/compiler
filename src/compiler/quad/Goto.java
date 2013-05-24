package compiler.quad;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Label;

public class Goto extends Quad {
	public Label label = null;

	public Goto(Label l) {
		label = l;
	}

	public String toString() {
		return "Goto " + label;
	}

	@Override
	public boolean isJump() {
		return true;
	}

	@Override
	public Label jumpLabel() {
		return label;
	}

	@Override
	public void replaceLabelOf(Label old, Label l) {
		if (label.equals(old)) {
			label = l;
		}
	}

	@Override
	public Quad jumpTargetIn(List<Quad> quads) {
		return findTargetIn(quads, label);
	}

	@Override
	public AssemList gen() {
		return L(new Assem("j %", label));
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
