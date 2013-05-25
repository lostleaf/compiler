package compiler.quad;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Addr;
import compiler.temp.IntConstant;
import compiler.temp.Label;
import compiler.temp.Temp;

public class Branch extends Quad {

	public static int EQUAL = 0, UNEQUAL = 1;

	// private int type;
	private String cmp;
	private Addr left;
	private Addr right;
	private Label label;

	public Branch(String cmp, Addr left, Addr right, Label label) {
		this.cmp = cmp;
		this.left = left;
		this.right = right;
		this.label = label;
	}

	public String toString() {
		if (right instanceof IntConstant && ((IntConstant) right).value == 0)
			return "b" + cmp + "z " + left + " goto " + label;
		return "b" + cmp + " " + left + " " + right + " goto " + label;
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
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if (left instanceof Temp)
			set.add((Temp)left);
		if (right instanceof Temp) {
			set.add((Temp) right);
		}
		return set;
	}

	@Override
	public AssemList gen() {
		if (right instanceof IntConstant && ((IntConstant) right).value == 0)
			return L(new Assem("b%z %, %", cmp, left, label));
		return L(new Assem("b% %, %, %", cmp, left, right, label));
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
		if (left.equals(old)) {
			left = t;
		}
		if (right instanceof Temp && right.equals(old)) {
			right = t;
		}
	}
}
