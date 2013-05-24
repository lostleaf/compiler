package compiler.assem;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import compiler.quad.Quad;
import compiler.regAlloc.RegAlloc;
import compiler.translate.CompilationUnit;

public class Codegen {

	private LinkedList<Assem> inits = new LinkedList<Assem>();

	public void gen(Assem assem) {
		inits.add(assem);
	}

	public void gen(AssemList assems) {
		for (AssemList p = assems; p != null; p = p.tail) {
			gen(p.head);
		}
	}

	private ArrayList<String> lines = new ArrayList<String>();

	public void gen(CompilationUnit cu, RegAlloc allocator) {
		ArrayList<Quad> quads = new ArrayList<Quad>(cu.getQuads());

		// AddStoreZero.process(quads);

		ArrayList<Assem> assems = new ArrayList<Assem>();
		for (Quad q : quads) {
			for (AssemList p = q.gen(); p != null; p = p.tail) {
				assems.add(p.head);
			}
		}

		// RemoveTopLevelDisplay.process(assems);
		//
		// for (Assem assem : assems) {
		// lines.add(StrengthReduction.process(assem).toString(allocator));
		// }
	}

	public void flush(PrintStream out, boolean opt) {
		ArrayList<String> insertsMain = new ArrayList<String>();

		// if (opt) {
		// ArrayList<String> oldLines = new ArrayList<String>();
		// do {
		// oldLines.clear();
		// oldLines.addAll(lines);
		// PeepholeOptimization.process(lines);
		// // System.out.println("\t[INFO] Peephole Optimized");
		// }
		// while (!oldLines.equals(lines));
		//
		// TreeSet<String> freeRegs = PeepholeOptimization.findFreeRegs(lines);
		//
		// ArrayList<String> topLoads =
		// PeepholeOptimization.findTopLoads(lines);
		// for (String loadSuffix : topLoads) {
		// if (freeRegs.isEmpty()) break;
		// if (PeepholeOptimization.checkTopLoad(lines, loadSuffix)) {
		// String reg = freeRegs.pollFirst();
		// PeepholeOptimization.replaceTopLoads(lines, loadSuffix, reg);
		// // System.out.println("\t[INFO] Peephole " + reg + " =" +
		// loadSuffix);
		// }
		// }
		// PeepholeOptimization.processBySuffix(lines);
		//
		// ArrayList<Integer> topConsts =
		// PeepholeOptimization.findTopConsts(lines);
		// for (int c : topConsts) {
		// String reg = "";
		// if (c == 0) {
		// reg = "$zero";
		// }
		// else {
		// if (freeRegs.isEmpty()) break;
		// reg = freeRegs.pollFirst();
		// insertsMain.add("\tli " + reg + ", " + c);
		// }
		// PeepholeOptimization.replaceTopConsts(lines, c, reg);
		// }
		// PeepholeOptimization.processPeepli(lines);
		//
		// PeepholeOptimization.inlineSyscall(lines);
		// }
		//
		// for (Assem init : inits) {
		// out.println(init);
		// }
		// for (String line : lines) {
		// out.println(line);
		// if (line.startsWith("main:")) {
		// for (String insert : insertsMain) {
		// out.println(insert);
		// }
		// }
		// }
	}
}
