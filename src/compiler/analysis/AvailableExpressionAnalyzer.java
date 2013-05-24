package compiler.analysis;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import compiler.quad.Quad;

public class AvailableExpressionAnalyzer {

	public void analyze(LinkedList<BasicBlock> blocks) {
		Set<Expression> U = new LinkedHashSet<Expression>();
		for (BasicBlock n : blocks) {
			U.addAll(n.genExp());
		}
		
		for (BasicBlock n : blocks) {
			n.avail.clear();
			n.avail.addAll(U);
		}
		
		boolean changed = true;
		while (changed) {
			changed = false;
			for (BasicBlock n : blocks) {
				if (n.getPredecessors().isEmpty()) {
					n.avail.clear();
				}
				else {
					for (BasicBlock p : n.getPredecessors()) {
						Set<Expression> x = new LinkedHashSet<Expression>(p.avail);
						x.addAll(p.genExp());
						x.removeAll(p.killExp(U));
						x.retainAll(n.avail);
						if (!n.avail.equals(x)) {
							n.avail = x;
							changed = true;
						}
					}
				}
			}
		}
		
		for (BasicBlock n : blocks) {
			Quad last = null;
			for (Quad q : n.getQuads()) {
				if (last == null) {
					q.avail = n.avail;
				}
				else {
					q.avail.clear();
					q.avail.addAll(last.avail);
					q.avail.addAll(last.genExp());
					q.avail.removeAll(last.killExp(U));
				}
				last = q;
			}
		}
	}
}
