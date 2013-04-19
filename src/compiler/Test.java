package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.google.gson.Gson;
import compiler.absyn.Program;
import compiler.parser.CLexer;
import compiler.parser.CParser;
import compiler.parser.CParser.ProgramContext;
import compiler.semantic.Semantic;
import compiler.util.Error;

public class Test {
	final static String filename = "5100379071-puyouer-lvalue2.c";
	final static String foldername = "testcases";

	public static void doit() throws Exception {
		CommonTokenStream cts = new CommonTokenStream(new CLexer(
				new ANTLRFileStream(foldername + "/" + filename)));
		CParser cp = new CParser(cts);
		cts.fill();

		ProgramContext context = cp.program();

		//context.save(cp, filename + ".ps");
		Program program = context.ret;
		 Gson gson = new Gson();
		 System.out.println(gson.toJson(program));
		Semantic semantic = new Semantic();
		semantic.checkProgram(program);
		for (Error error : semantic.getErrors())
			System.out.println(error);
		if (semantic.hasError())
			exit(1);
		exit(0);
	}

	private static void exit(int i) {
		System.out.println("exit with " + i);
		System.exit(i);
	}

	private static void fuckit(String inputName) {
		Scanner cin = null;

		try {
			cin = new Scanner(new File(inputName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		int ans = 0;
		while (cin.hasNext()) {
			String line = cin.nextLine();
			// System.out.println(line);
			if (line.contains("int") || line.contains("struct")) {
				ans = 1;
				break;
			}

			if (line.contains("#")) {
				ans = 0;
				break;
			}
		}

		System.out.println("Don't Panic");
		System.exit(ans);
	}

	public static void main(String[] args) throws Exception {
		doit();
	}

}
