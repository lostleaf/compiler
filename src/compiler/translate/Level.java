package compiler.translate;

import java.util.TreeSet;

import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.Temp;
import compiler.util.Config;

public class Level implements Config {
	
	public Level parent = null;
	public int depth = 0;
	public static int maxDepth = 0;
	
	public Level() {
		depth = 0;
	}
	
	public Level(Level parentLevel) {
		parent = parentLevel;
		depth = parent.depth + 1;
		if (depth > maxDepth) {
			maxDepth = depth;
		}
	}
	
	private int numOfLocals = 0;
	
	public Temp newLocal() {
		return new Temp(this, numOfLocals++);
	}

	public int frameSize() {
		return wordSize * (savedRegistersOffset + numOfSavedRegisters + numOfLocals);
	}
	
	private TreeSet<Integer> usedRegisters = new TreeSet<Integer>();

	public void useRegister(int r) {
		usedRegisters.add(r);
	}

	public AssemList saveRegisters() {
		AssemList saves = null;
		for (int i = 0; i < numOfSavedRegisters; i++) {
			int r = baseOfSavedRegisters + i;
			if (usedRegisters.contains(r)) {
				saves = new AssemList(
						new Assem("sw $%, %($sp)", regNames[r], (numOfLocals + i) * wordSize),
						saves);
			}
		}
		return saves;
	}

	public AssemList loadRegisters() {
		AssemList loads = null;
		for (int i = 0; i < numOfSavedRegisters; i++) {
			int r = baseOfSavedRegisters + i;
			if (usedRegisters.contains(r)) {
				loads = new AssemList(
						new Assem("lw $%, %($sp)", regNames[r], (numOfLocals + i) * wordSize),
						loads);
			}
		}
		return loads;
	}
}
