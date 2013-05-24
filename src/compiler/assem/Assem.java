package compiler.assem;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import compiler.regAlloc.DefaultMap;
import compiler.regAlloc.RegAlloc;
import compiler.temp.Temp;
import compiler.util.Config;


public class Assem implements Config {
	
	public String format;
	public Object[] params;
	
	public Assem(String format, Object... params) {
		this.format = format;
		this.params = params;
	}
	
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		for (int i = 0, j = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '@' || c == '%') {
				if (c == '@' && params[j] instanceof Temp) {
					set.add((Temp) params[j]);
				}
				++j;
			}
		}
		return set;
	}
	
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		for (int i = 0, j = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '@' || c == '%') {
				if (c == '%' && params[j] instanceof Temp) {
					set.add((Temp) params[j]);
				}
				++j;
			}
		}
		return set;
	}
	
	public String toString() {
		return toString(DefaultMap.getSingleton());
	}
	
	public String toString(RegAlloc allocator) {
		return isSpill() ? doSpill(allocator) : doNormal(allocator);
	}
	
	private String doNormal(RegAlloc allocator) {
		StringBuffer buf = new StringBuffer();
		
		if (format.charAt(0) == '!') {
			format = format.substring(1);
		}
		else {
			buf.append('\t');
		}
		
		for (int i = 0, j = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '@' || c == '%') {
				if (params[j] instanceof Temp) {
					buf.append(allocator.map((Temp) params[j]));
				}
				else {
					buf.append(params[j]);
				}
				++j;
			}
			else {
				buf.append(c);
			}
		}
		return buf.toString();
	}
	
	private boolean isSpill() {
		for (Object param : params) {
			if (param instanceof Temp && ((Temp) param).getLiveInterval().spilled) {
				return true;
			}
		}
		return false;
	}
	
	private String doSpill(RegAlloc allocator) {
		StringBuffer before = new StringBuffer();
		StringBuffer after = new StringBuffer();
		
		TreeSet<Integer> freeRegs = new TreeSet<Integer>();
		freeRegs.add(26);	// $k0
		freeRegs.add(27);	// $k1
		
		for (Temp t : use()) {
			if (t.getLiveInterval().register == spillReg) {
				int r = freeRegs.pollFirst();
				t.getLiveInterval().register = r;
				// lw $r, wordSize*t.home.depth($gp)
				// lw $r, wordSize*t.index($r)
				//before.append("\t\t\tlw $" + regNames[r] + ", " + wordSize * t.home.depth + "($gp)\t# load display for spilling\n");
				before.append("\t\t\tlw $" + regNames[r] + ", " + wordSize * t.index + "($" + regNames[r] + ")\t# load for spilling\n");

			}
		}
		
		for (Temp t : def()) {
			if (t.getLiveInterval().register == spillReg) {
				int r = freeRegs.pollFirst();
				t.getLiveInterval().register = r;
			}
		}
		
		// possibly a function call
		String normal = doNormal(allocator);

		for (Temp t : def()) {
			if (t.getLiveInterval().spilled && t.getLiveInterval().register != spillReg) {
				int r = t.getLiveInterval().register;
				t.getLiveInterval().register = spillReg;
				int d = 26+27 - r;	// another $k?
				// lw $d, wordSize*t.home.depth($gp)
				// sw $r, wordSize*t.index($d)
				//after.append("\n\t\t\tlw $" + regNames[d] + ", " + wordSize * t.home.depth + "($gp)\t# load display for spilling");
				after.append("\n\t\t\tsw $" + regNames[r] + ", " + wordSize * t.index + "($" + regNames[d] + ")\t# write back for spilling");
			}
		}
		
		for (Temp t : use()) {
			if (t.getLiveInterval().spilled && t.getLiveInterval().register != spillReg) {
				t.getLiveInterval().register = spillReg;
			}
		}
		
		return before + normal + after;
	}
}
