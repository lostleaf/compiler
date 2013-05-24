package compiler.translate;

import java.util.LinkedHashSet;
import java.util.Set;

import compiler.analysis.Expression;
import compiler.assem.Assem;
import compiler.assem.AssemList;
import compiler.quad.Quad;
import compiler.temp.Label;
import compiler.temp.Temp;

public class DataFrag extends Quad {

	Label label = null;
	String value = null;
	
	public DataFrag(Label l, String s) {
		label = l;
		value = s;
	}

	@Override
	public Set<Temp> def() {
		return new LinkedHashSet<Temp>();
	}

	@Override
	public Set<Temp> use() {
		return new LinkedHashSet<Temp>();
	}

	@Override
	public AssemList gen() {
//		return L(new Assem("!%:", label),
//				L(new Assem(".word %", value.length()),
//				L(new Assem(".ascii \"%\"", escapeSpecialChars(value)),
//				L(new Assem(".align 2")))));
		return L(new Assem("!%:", label),
				L(new Assem(".asciiz \"%\"", escapeSpecialChars(value)),
				L(new Assem(".align 2"))));
	}

	private static String escapeSpecialChars(String s) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\n') {
				buf.append("\\n");
			}
			else if (c == '\t') {
				buf.append("\\t");
			}
			else if (c == '\"') {
				buf.append("\\\"");
			}
			else if (c == '\r') {
				buf.append("\\r");
			}
			else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	@Override
	public Set<Expression> genExp() {
		return new LinkedHashSet<Expression>();
	}

	@Override
	public Set<Expression> killExp(Set<Expression> U) {
		return new LinkedHashSet<Expression>();
	}

	@Override
	public void replaceUseOf(Temp old, Temp t) {
	}
}
