package compiler.quad;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Addr;
import compiler.temp.Const;
import compiler.temp.Label;
import compiler.temp.Temp;

public class IfFalse extends Quad {

	public Addr addr = null;
	public Label label = null;

	public IfFalse(Addr a, Label f) {
		label = f;
		addr = a;
	}

	public String toString() {
		return "IfFalse " + addr + " Goto " + label;
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
		if (addr instanceof Temp)
			set.add((Temp) addr);
		return set;
	}

	@Override
	public AssemList gen() {
		Object ad = addr;
		if (Temp.sp.equals(ad))
			ad = "$sp";
		if (Temp.gp.equals(ad))
			ad = "$gp";
		if (Temp.v1.equals(ad))
			ad = "$v1";
		return L(new Assem("beqz %, %", ad, label));
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

	@Override
	public void replaceUseOf(Temp old, Const t) {
		if (addr.equals(old)) {
			addr = t;
		}
	}

}
