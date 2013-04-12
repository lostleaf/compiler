package compiler.type;

import java.util.ArrayList;
import java.util.List;

import compiler.symbol.Symbol;



public class RECORD extends TYPE {

	public static final class RecordField {

		public TYPE type;
		public Symbol name;
		public int index;

		public RecordField(TYPE type, Symbol name, int index) {
			this.type = type;
			this.name = name;
			this.index = index;
		}
	}

	public List<RecordField> fields;
	public Symbol name;

	public RECORD(Symbol name) {
		fields = new ArrayList<RecordField>();
		this.name = name;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof RECORD) {
			return name.equals(((RECORD) other).name);
		}
		return false;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public boolean isRecord() {
		return true;
	}

}
