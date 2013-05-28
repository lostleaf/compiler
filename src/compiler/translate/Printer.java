package compiler.translate;

import java.io.PrintStream;

import compiler.analysis.BasicBlock;
import compiler.quad.LabelQuad;
import compiler.quad.Quad;

public class Printer {

	private PrintStream out;
	
	public Printer(PrintStream printStream) {
		out = printStream;
	}

	public void print(Quad q) {
		if (q instanceof LabelQuad)
			out.println(q);
		else
			out.println("\t" + q);
	}

	public void print(BasicBlock bb) {
		for (Quad q : bb.getQuads()) {
			print(q);
		}
	}
	
	public void printLiveness(Quad q) {
		out.println("IN:" + q.IN + "\tOUT:" + q.OUT);
	}
	
	public void printLiveness(BasicBlock bb) {
		for (Quad q : bb.getQuads()) {
			printLiveness(q);
		}
	}
}
