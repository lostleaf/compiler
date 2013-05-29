package compiler;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

	static String fileName = "multiarray-5100309153-yanghuan.c";
	static Program program = null;
	static Translate translate = null;

	static void exit(int a) {
		System.out.println("exit with " + a);
	}

	public static void main(String[] args) throws Exception {
		if (args.length > 0)
			fileName = args[0];
		try {
			syntactics();
		} catch (Exception e) {
			System.out.println("1");
			throw e;
		}
		Semantic semantic = new Semantic();
		semantic.checkProgram(program);
		for (Error error : semantic.getErrors())
			System.out.println(error);
		if (semantic.hasError())
			exit(1);

		System.out.println("0");

		translate = new Translate();
		translate.transProgram(program);

		FileWriter writer = new FileWriter(fileName + ".q", false);
		writer.write("-------------StaticDataFrag---------------\n");
		for (DataFrag sd : translate.dataFrags)
			writer.write(sd + "\n");
		for (int i = 0; i < translate.cus.size(); ++i) {
			writer.write("---------CU" + i + "----------\n");
			for (Quad q : translate.cus.get(i).quads)
				writer.write(q + "\n");
		}
		writer.close();
		codeGeneration(true);
		System.exit(0);

	}

	public static void codeGeneration(boolean opt) throws Exception {
		Analyzer analyzer = new Analyzer();
		// LabelEliminator le = new LabelEliminator();

		for (CompilationUnit u : translate.cus) {
			// while (le.eliminate(u))
			// ;
			u.findBranches(analyzer);
			u.findBasicBlocks(analyzer);
			u.findLiveness(analyzer);
		}
		if (opt) {
			// LocalCopyPropagation lcp = new LocalCopyPropagation();
			// DeadCodeEliminator dce = new DeadCodeEliminator();

			for (CompilationUnit u : translate.cus) {

				List<Quad> oldQuads = new LinkedList<Quad>();
				do {
					oldQuads.clear();
					oldQuads.addAll(u.getQuads());
					List<Quad> nqs = new LinkedList<Quad>();
					for (BasicBlock bb : u.getBasicBlocks()) {
						// lcp.propagate(bb);
						for (Iterator<Quad> it = bb.getQuads().iterator(); it
								.hasNext();)
							nqs.add((Quad) (it.next()));
					}
					u.setQuads(nqs);

					u.findBasicBlocks(analyzer);
					u.findLiveness(analyzer);
					// dce.eliminate(u);

					u.findBasicBlocks(analyzer);
					u.findLiveness(analyzer);
				} while (!u.getQuads().equals(oldQuads));
			}
		}
		FileWriter writer = new FileWriter(fileName + ".qq", false);
		writer.write("-------------StaticDataFrag---------------\n");
		for (DataFrag sd : translate.dataFrags)
			writer.write(sd + "\n");
		for (int i = 0; i < translate.cus.size(); ++i) {
			writer.write("---------CU" + i + "----------\n");
			for (Quad q : translate.cus.get(i).quads)
				writer.write(q + "\n");
		}
		writer.close();
		Codegen codegen = new Codegen();

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

		for (int i = 1; i < translate.cus.size(); ++i) {
			codegen.gen(translate.cus.get(i),
					new LinearScan(translate.cus.get(i), analyzer));
		}

		// ======================================== Output
		// ========================================//

		PrintStream out = new PrintStream(new BufferedOutputStream(
				new FileOutputStream(fileName + ".s")));
		out.println("########################################");
		out.println("############### CODE GEN ###############");
		out.println("########################################");
		codegen.flush(out, false);
		printRuntime(out, false);
		out.close();
	}

	private static void printRuntime(PrintStream out, boolean gc)
			throws FileNotFoundException {
		String runtime_s = "/runtime.s";
		// System.out.println(Main.class.getResource("/runtime.s"));
		Scanner scanner = new Scanner(Main.class.getResourceAsStream(runtime_s));
		while (scanner.hasNextLine()) {
			out.println(scanner.nextLine());
		}
		scanner.close();
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
		//context.save(parser, fileName+ ".ps");
		program = context.ret;

	}
}
