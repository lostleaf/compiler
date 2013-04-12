package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import com.google.gson.Gson;
import compiler.absyn.Program;
import compiler.parser.CLexer;
import compiler.parser.CParser;
import compiler.parser.CParser.ProgramContext;

public class Test {
	final static String filename = "example2.c";

	public static void doit() throws Exception {
		CommonTokenStream cts = new CommonTokenStream(new CLexer(
				new ANTLRFileStream(filename)));
		CParser cp = new CParser(cts);
		cts.fill();

		ProgramContext context = cp.program();

		// tree.save(cp, filename + ".ps");
		Program program = context.ret;
		Gson gson = new Gson();
		System.out.println(gson.toJson(program));
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
		// System.out.println(ans);
		/*
		 * Random random = new Random(); if (random.nextInt() % 100 < 3) ans = 1
		 * - ans;
		 */
		System.out.println("Don't Panic");
		System.exit(ans);
	}

	public static void main(String[] args) throws Exception {
		doit();
	}

}
