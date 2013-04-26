package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
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

public class MidTerm {
	final static boolean DEBUG = true;
	final static String TESTCASE_FOLDER = "testcases";

	public static void doit(File testcase) throws Exception {
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
		if (semantic.hasError())
			exit(1);
		else
			exit(0);
	}

	private static void exit(int i) {
		System.out.println("exit with " + i);
		if (!DEBUG)
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

		// System.out.println("Don't Panic");
		System.exit(ans);
	}

	public static void main(String[] args) throws Exception {
		if (DEBUG) {
			File[] testcases = new File(TESTCASE_FOLDER)
					.listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							int index = name.indexOf('.');
							if (index == -1)
								return false;
							return name.substring(index).equals(".c");
						}
					});
			for (File testcase : testcases) {
				System.out.print(testcase.getName() + "   ");
				doit(testcase);
			}
		} else
			doit(new File(args[0]));
		// System.out.println(testcase.getAbsolutePath());
	}

}
