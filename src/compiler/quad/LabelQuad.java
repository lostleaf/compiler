package compiler.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Label;

public class LabelQuad extends Quad {
	Label label;

	public LabelQuad(Label l) {
		label = l;
	}

	public LabelQuad() {
		this(new Label());
	}

	@Override
	public String toString() {
		return label + ":";
	}

	@Override
	public AssemList gen() {
		return L(new Assem("!%:", label));
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
