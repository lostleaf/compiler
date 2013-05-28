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
		p.println(translate.cus.size());
		for (int i = 0; i < translate.cus.size(); i++) {
			p.println("\n\n" + "cu " + i + "\n");

			for (Quad q : translate.cus.get(i).quads)
				p.println(q);
		}
		p.close();
	}

	public static void main(String args[]) {
		try {
			doTranslate(new File("queens.c"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
