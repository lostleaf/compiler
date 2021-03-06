package compiler.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import compiler.absyn.BinOp;
import compiler.quad.Binop;
import compiler.quad.Branch;
import compiler.quad.Enter;
import compiler.quad.Goto;
import compiler.quad.IfFalse;
import compiler.quad.Leave;
import compiler.quad.Quad;
import compiler.temp.Temp;
import compiler.util.Config;

public class Analyzer implements Config {

	public List<Quad> findBranches(List<Quad> quads) {
		List<Quad> a = new ArrayList<Quad>(quads);

		for (int i = 1; i < a.size(); i++) {
			if (a.get(i) instanceof IfFalse) {
				IfFalse f = (IfFalse) a.get(i);
				if (a.get(i - 1) instanceof Binop) {
					Binop b = (Binop) a.get(i - 1);
					if (f.addr.equals(b.target)
							&& b.op.ordinal() >= BinOp.LESS.ordinal()
							&& b.op.ordinal() <= BinOp.NEQ.ordinal()) {
						a.set(i, new Branch(ifFalseCmp[b.op.ordinal()
								- BinOp.LESS.ordinal()], b.left, b.right,
								f.label));
						a.remove(i - 1);
					}
				}
				// else if (a.get(i-1) instanceof BinopI) {
				// BinopI b = (BinopI) a.get(i-1);
				// if (f.addr.equals(b.dest) && b.oper >= OpExp.EQ) {
				// a.set(i, new Branch(ifFalseCmp[b.oper], b.left, b.right,
				// f.label));
				// a.remove(i-1);
				// }
				// }
			}
		}

		return a;
	}

	/**
	 * Step 1. Identify the leaders in the code. Leaders are instructions which
	 * come under any of the following 3 categories: * The first instruction is
	 * a leader. * The target of a conditional or an unconditional goto/jump
	 * instruction is a leader. * The instruction that immediately follows a
	 * conditional or an unconditional goto/jump instruction is a leader. Step
	 * 2. Starting from a leader, the set of all following instructions until
	 * and not including the next leader is the basic block corresponding to the
	 * starting leader.
	 */
	public LinkedList<BasicBlock> getBasicBlocks(List<Quad> quads) {
		identifyLeaders(quads);
		return buildBlocks(quads);
	}

	private void identifyLeaders(List<Quad> quads) {
		quads = new ArrayList<Quad>(quads);

		// clean up
		for (int i = 0; i < quads.size(); i++) {
			quads.get(i).clearAll();
		}

		for (int i = 0; i < quads.size() - 1; i++) {
			Quad q = quads.get(i);
			if (!(q instanceof Goto || q instanceof Leave)) {
				q.addSuccessor(quads.get(i + 1));
			}
		}

		// gives the basic block `ENTRY'
		quads.get(0).setLeader(); // function label
		if (quads.size() > 1 && quads.get(1) instanceof Enter)
			quads.get(2).setLeader(); // instruction after `enter'

		// gives the basic block `EXIT'
		for (Quad q : quads) {
			if (q instanceof Leave) {
				q.setLeader();
			}
		}

		for (int i = 0; i < quads.size() - 1; i++) {
			Quad q = quads.get(i);
			if (q.isJump()) {
				Quad t = q.jumpTargetIn(quads);
				t.setLeader();
				q.addSuccessor(t);
				quads.get(i + 1).setLeader();
			}
			if (q instanceof Leave)
				quads.get(i + 1).setLeader();
		}
	}

	private LinkedList<BasicBlock> buildBlocks(List<Quad> quads) {
		LinkedList<BasicBlock> blocks = new LinkedList<BasicBlock>();
		BasicBlock currentBlock = null;
		for (Quad q : quads) {
			if (q.isLeader()) {
				if (currentBlock != null) {
					blocks.add(currentBlock);
				}
				currentBlock = new BasicBlock();
			}
			currentBlock.addQuad(q);
		}
		if (currentBlock != null) {
			blocks.add(currentBlock);
		}

		for (BasicBlock bb : blocks) {
			Quad last = bb.getLastQuad();
			for (Quad succ : last.getSuccessors()) {
				addFlowEdge(bb, findBlockByQuad(blocks, succ));
			}
		}

		return blocks;
	}

	private void addFlowEdge(BasicBlock from, BasicBlock to) {
		from.addSuccessor(to);
		to.addPredecessor(from);
	}

	private BasicBlock findBlockByQuad(List<BasicBlock> blocks, Quad q) {
		for (BasicBlock bb : blocks) {
			if (bb.getFirstQuad().equals(q)) {
				return bb;
			}
		}
		// System.out.println("ERROR: can not find a block start with q = \"" +
		// q + "\"");
		return null;
	}

	/**
	 * By adding loop inspect variable in the while statement, it shows that the
	 * algorithm loops at most 3 times.
	 */
	public void findLiveness(LinkedList<BasicBlock> blocks) {
		LinkedList<BasicBlock> bb = new LinkedList<BasicBlock>();
		Iterator<BasicBlock> iter = blocks.descendingIterator();
		//iter.next(); // skip EXIT (the BasicBlock with `leave LLL')
		while (iter.hasNext()) {
			bb.add(iter.next());
		}

		/**
		 * Liveness may be calculated many times. So clear the sets every time.
		 */
		for (BasicBlock b : bb) {
			b.IN.clear();
			b.OUT.clear();
			for (Quad q : b.getQuads()) {
				q.IN.clear();
				q.OUT.clear();
			}
		}

		boolean changed = true;
		while (changed) {
			changed = false;
			for (BasicBlock B : bb) {
				B.OUT.clear();
				for (BasicBlock S : B.getSuccessors()) {
					B.OUT.addAll(S.IN);
				}

				Set<Temp> oldIN = new LinkedHashSet<Temp>(B.IN);

				B.IN.clear();
				B.IN.addAll(B.OUT);
				B.IN.removeAll(B.def());
				B.IN.addAll(B.use());

				if (!oldIN.equals(B.IN)) {
					changed = true;
				}
			}
		}

		for (BasicBlock B : bb) {
			findLiveness(B.getQuads(), B.OUT);
		}
	}

	private void findLiveness(LinkedList<Quad> quads, Set<Temp> OUT) {
		Iterator<Quad> iter = quads.descendingIterator();
		Quad last = null;
		while (iter.hasNext()) {
			Quad q = iter.next();
			q.OUT = (last == null) ? OUT : last.IN;
			q.IN.addAll(q.OUT);
			q.IN.removeAll(q.def());
			q.IN.addAll(q.use());
			last = q;
		}
	}

	public LinkedHashMap<Temp, LiveInterval> findLiveIntervals(List<Quad> quads) {
		for (Quad q : quads) {
			for (Temp t : q.IN) {
				t.clearLiveInterval();
			}
			for (Temp t : q.OUT) {
				t.clearLiveInterval();
			}
			for (Temp t : q.use()) {
				t.clearLiveInterval();
			}
			for (Temp t : q.def()) {
				t.clearLiveInterval();
			}
		}
		LinkedHashMap<Temp, LiveInterval> intervals = new LinkedHashMap<Temp, LiveInterval>();
		int qcount = 0;
		for (Quad q : quads) {
			++qcount;
			for (Temp t : q.IN) {
				t.expandInterval(qcount);
				intervals.put(t, t.getLiveInterval());
			}
			for (Temp t : q.OUT) {
				t.expandInterval(qcount);
				intervals.put(t, t.getLiveInterval());
			}
			for (Temp t : q.use()) {
				t.expandInterval(qcount);
				intervals.put(t, t.getLiveInterval());
			}
			for (Temp t : q.def()) {
				t.expandInterval(qcount);
				intervals.put(t, t.getLiveInterval());
			}
		}
		return intervals;
	}
}