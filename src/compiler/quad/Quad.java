package compiler.quad;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Const;
import compiler.temp.Label;
import compiler.temp.Temp;
import compiler.util.Config;

public abstract class Quad implements Config {
	String cmt = "";

	public void clearAll() {
		leader = false;
		successors.clear();
		IN.clear();
		OUT.clear();
		// avail.clear();
	}

	private boolean leader = false;

	public void setLeader() {
		leader = true;
	}

	public boolean isLeader() {
		return leader;
	}

	public boolean isJump() {
		return false;
	}

	public Label jumpLabel() {
		return null;
	}

	public void replaceLabelOf(Label old, Label l) {
	}

	public Quad jumpTargetIn(List<Quad> quads) {
		return null;
	}

	protected Quad findTargetIn(List<Quad> quads, Label target) {
		for (Quad q : quads) {
			if (q instanceof LabelQuad && ((LabelQuad) q).label.equals(target)) {
				return q;
			}
		}
		return null;
	}

	public Set<Temp> def() {
		return new LinkedHashSet<Temp>();
	}

	public Set<Temp> use() {
		return new LinkedHashSet<Temp>();
	}

	public void replaceUseOf(Temp old, Temp t) {
	}

	public void replaceUseOf(Temp old, Const t) {
	}

	public Quad promotion() {
		return this;
	}

	private List<Quad> successors = new LinkedList<Quad>();

	public void addSuccessor(Quad q) {
		successors.add(q);
	}

	public List<Quad> getSuccessors() {
		return successors;
	}

	public Set<Temp> IN = new LinkedHashSet<Temp>();
	public Set<Temp> OUT = new LinkedHashSet<Temp>();

	public Set<Expression> avail = new LinkedHashSet<Expression>();

	public abstract Set<Expression> genExp();

	public abstract Set<Expression> killExp(Set<Expression> U);

	protected Set<Expression> killExpBy(Set<Expression> U, Temp t) {
		Set<Expression> x = new LinkedHashSet<Expression>();
		for (Expression e : U) {
			if (e.isKilledBy(t)) {
				x.add(e);
			}
		}
		return x;
	}

	public abstract AssemList gen();

	protected static AssemList L(Assem h, AssemList t) {
		return AssemList.L(h, t);
	}

	protected static AssemList L(Assem h) {
		return AssemList.L(h);
	}

	protected static AssemList L(AssemList a, AssemList b) {
		return AssemList.L(a, b);
	}

	public Quad commentWith(String c) {
		cmt = c;
		return this;
	}
}
