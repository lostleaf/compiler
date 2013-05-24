package compiler.translate;

import java.io.File;
import java.io.PrintWriter;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import compiler.absyn.Program;
import compiler.parser.CLexer;
import compiler.parser.CParser;
import compiler.parser.CParser.ProgramContext;
import compiler.quad.Quad;
import compiler.semantic.Semantic;
import compiler.util.Error;

public class TestTranslate {

	public static void doTranslate(File testcase) throws Exception {
		CommonTokenStream cts = new CommonTokenStream(new CLexer(
				new ANTLRFileStream(testcase.getAbsolutePath())));
		CParser cp = new CParser(cts);
		cts.fill();

		ProgramContext context = cp.program();

		// context.save(cp, filename + ".ps");
		Program program = context.ret;
		// Gson gson = new Gson();
		// System.out.println(gson.toJson(program));
		Semantic semantic = new Semantic();
		semantic.checkProgram(program);
		for (Error error : semantic.getErrors())
			System.out.println(error);
		if (semantic.hasError()) {
			System.out.println("semantic error");
			System.exit(1);
		}

		Translate translate = new Translate();
		translate.transProgram(program);

		PrintWriter p = new PrintWriter("result.txt");
		p.println(translate.topLevelQuads.size() + " " + translate.quads.size());
		p.print("toplevel:");
		for (Quad q : translate.topLevelQuads)
			p.println(q.getClass().toString() + " " + q);

		p.println("\n-------\n");
		for (Quad q : translate.quads)
			p.println(q.getClass().toString() + " " + q);
		p.close();
	}

	public static void main(String args[]) {
		try {
			doTranslate(new File("example1.c"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
