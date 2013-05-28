package compiler.analysis;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import compiler.quad.Quad;
import compiler.temp.Temp;

public class BasicBlock {
	
	public static int count = 0;
	private int number = 0;
	
	public BasicBlock() {
		number = ++count;
	}
	
	private LinkedList<Quad> quads = new LinkedList<Quad>();

	public void addQuad(Quad q) {
		quads.add(q);
	}

	public LinkedList<Quad> getQuads() {
		return quads;
	}

	public Quad getLastQuad() {
		return quads.descendingIterator().next();
	}

	public Object getFirstQuad() {
		return quads.get(0);
	}
	
	private LinkedList<BasicBlock> successors = new LinkedList<BasicBlock>();

	public void addSuccessor(BasicBlock succ) {
		successors.add(succ);
	}
	
	public LinkedList<BasicBlock> getSuccessors() {
		return successors;
	}
	
	private LinkedList<BasicBlock> predecessors = new LinkedList<BasicBlock>();

	public void addPredecessor(BasicBlock pred) {
		predecessors.add(pred);
	}
	
	public LinkedList<BasicBlock> getPredecessors() {
		return predecessors;
	}
	
	public String toString() {
		return "B" + number;
	}
	
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		Iterator<Quad> iter = quads.descendingIterator();
		while (iter.hasNext()) {
			Quad q = iter.next();
			set.addAll(q.def());
			set.removeAll(q.use());
		}
		return set;
	}
	
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		Iterator<Quad> iter = quads.descendingIterator();
		while (iter.hasNext()) {
			Quad q = iter.next();
			set.removeAll(q.def());
			set.addAll(q.use());
		}
		return set;
	}
	
	public Set<Temp> IN = new LinkedHashSet<Temp>();
	public Set<Temp> OUT = new LinkedHashSet<Temp>();
	
	public Set<Expression> avail = new LinkedHashSet<Expression>();

	public Set<Expression> genExp() {
		Set<Expression> x = new LinkedHashSet<Expression>();
		for (Quad q : quads) {
			x.addAll(q.genExp());
		}
		return x;
	}
	
	public Set<Expression> killExp(Set<Expression> U) {
		Set<Expression> x = new LinkedHashSet<Expression>();
		for (Quad q : quads) {
			x.addAll(q.killExp(U));
		}
		return x;
	}
}
