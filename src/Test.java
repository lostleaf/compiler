import java.io.PrintWriter;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;

import parser.CLexer;
import parser.CParser;


public class Test {
	final static String filename = "example2.c";

	public static void doit() throws Exception {
		CommonTokenStream cts = new CommonTokenStream(new CLexer(
				new ANTLRFileStream(filename)));
		CParser cp = new CParser(cts);
		cts.fill();

		PrintWriter p = null;
		try {
			p = new PrintWriter(filename + ".tokens");
		} catch (Exception e) {
			System.err.println(e.toString());
		}

		for (Token tok : cts.getTokens())
			p.println(tok.toString());
		p.close();

		RuleContext tree = cp.program();
		tree.save(cp, filename + ".ps");

	}

	public static void main(String[] args) throws Exception {
		doit();
	}

}
