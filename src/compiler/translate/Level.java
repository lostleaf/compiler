package compiler.translate;

import java.util.TreeSet;

import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.temp.IntConstant;
import compiler.temp.Reference;
import compiler.temp.Temp;
import compiler.util.Config;

public class Level implements Config {
	// record number of used registers in IR
	public int index = 0;
	// record size of local var
	public int size = 0;

	public int frameSize() {
		return size + (index + numOfSavedRegisters + 1) * wordSize;
	}

	public Temp newTemp() {
		return new Temp(this, index++);
	}

	public Reference get() {
		return new Reference(Temp.sp, new IntConstant(size));
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
				saves = new AssemList(new Assem("sw $%, %($sp)", regNames[r],
						size + (index + i) * wordSize), saves);
			}
		}
		return saves;
	}

	public AssemList loadRegisters() {
		AssemList loads = null;
		for (int i = 0; i < numOfSavedRegisters; i++) {
			int r = baseOfSavedRegisters + i;
			if (usedRegisters.contains(r)) {
				loads = new AssemList(new Assem("lw $%, %($sp)", regNames[r],
						size + (index + i) * wordSize), loads);
			}
		}
		return loads;
	}
}
