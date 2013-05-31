package compiler;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import compiler.absyn.Program;
import compiler.analysis.Analyzer;
import compiler.analysis.BasicBlock;
import compiler.assem.Assem;
import compiler.assem.Codegen;
import compiler.parser.CLexer;
import compiler.parser.CParser;
import compiler.parser.CParser.ProgramContext;
import compiler.quad.Quad;
import compiler.regAlloc.LinearScan;
import compiler.semantic.Semantic;
import compiler.translate.CompilationUnit;
import compiler.translate.DataFrag;
import compiler.translate.Translate;
import compiler.util.Config;
import compiler.util.Error;

public class Main implements Config {
	static class CListener extends BaseErrorListener {
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
				Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			System.out.println("Parse Error" + line + " " + charPositionInLine
					+ " " + msg);
			e.printStackTrace();
			System.out.println("exit with 1");
			System.exit(1);
		}
	}

	static String fileName = "queens-5100379110-daibo.c";
	static Program program = null;
	static Translate translate = null;

	static void exit(int a) {
		// System.out.println("exit with " + a);
		System.exit(1);
	}

	private static void syntactics() throws Exception {
		ANTLRFileStream input = new ANTLRFileStream(fileName);
		CLexer lexer = new CLexer(input);
		CommonTokenStream cts = new CommonTokenStream(lexer);

		CParser parser = new CParser(cts);
		cts.fill();

		parser.removeErrorListeners();
		parser.addErrorListener(new CListener());
		ProgramContext context = parser.program();
		program = context.ret;

	}

	public static void main(String[] args) {
		if (args.length > 0)
			fileName = args[0];

		try {
			syntactics();
		} catch (Exception e) {
			System.out.println("1");
			exit(1);
		}
		Semantic semantic = new Semantic();
		semantic.checkProgram(program);
		for (Error error : semantic.getErrors())
			System.out.println(error);
		if (semantic.hasError())
			exit(1);

		translate = new Translate();
		translate.transProgram(program);

		codeGeneration(true);
	}

	private static void codegenInitialize(Codegen codegen, Analyzer analyzer) {
		codegen.gen(new Assem(".data"));
		for (DataFrag data : translate.dataFrags)
			codegen.gen(data.gen());
		codegen.gen(new Assem(".align 2"));
		codegen.gen(new Assem(".globl args"));
		codegen.gen(new Assem("!args:\t.space %", (translate.maxArgc + 1)
				* wordSize));
		codegen.gen(new Assem(".align 2"));

		codegen.gen(new Assem(".globl stat"));
		codegen.gen(new Assem("!stat:\t.space %", (translate.topLevelSize + 1)
				* wordSize));
		codegen.gen(new Assem(".text"));
		codegen.gen(new Assem(".align 2"));

		codegen.gen(new Assem(".globl main"));
		codegen.gen(new Assem("!main:"));
		codegen.gen(new Assem("la $gp, stat"));
		codegen.gen(new Assem("la $v1, args"));
		codegen.gen(translate.cus.get(0), new LinearScan(translate.cus.get(0),
				analyzer));
		codegen.gen(new Assem("jal exit"));
	}

	private static void printRuntime(PrintStream out, boolean gc) {
		String runtime_s = "/runtime.s";
		Scanner scanner = new Scanner(Main.class.getResourceAsStream(runtime_s));
		for (; scanner.hasNextLine(); out.println(scanner.nextLine()))
			;
		scanner.close();
	}

	private static void optimize(Analyzer analyzer) {
		for (CompilationUnit u : translate.cus) {

			List<Quad> oldQuads = new LinkedList<Quad>();
			do {
				oldQuads.clear();
				oldQuads.addAll(u.getQuads());
				List<Quad> nqs = new LinkedList<Quad>();
				for (BasicBlock bb : u.getBasicBlocks()) {
					for (Iterator<Quad> it = bb.getQuads().iterator(); it
							.hasNext();)
						nqs.add((Quad) (it.next()));
				}
				u.setQuads(nqs);

				u.findBasicBlocks(analyzer);
				u.findLiveness(analyzer);

				u.findBasicBlocks(analyzer);
				u.findLiveness(analyzer);
			} while (!u.getQuads().equals(oldQuads));
		}
	}

	public static void codeGeneration(boolean opt) {
		Analyzer analyzer = new Analyzer();

		for (CompilationUnit u : translate.cus) {
			u.findBranches(analyzer);
			u.findBasicBlocks(analyzer);
			u.findLiveness(analyzer);
		}

		optimize(analyzer);

		Codegen codegen = new Codegen();
		codegenInitialize(codegen, analyzer);

		for (int i = 1; i < translate.cus.size(); ++i) {
			codegen.gen(translate.cus.get(i),
					new LinearScan(translate.cus.get(i), analyzer));
		}

		PrintStream output = System.out;
		// new PrintStream(new BufferedOutputStream(new
		// FileOutputStream(fileName + ".s")));
		printCodegenBanner(output);
		codegen.flush(output, false);
		printRuntime(output, false);
		output.close();
	}

	private static void printCodegenBanner(PrintStream output) {
		output.println("########################################");
		output.println("############### CODE GEN ###############");
		output.println("########################################");
	}

}
