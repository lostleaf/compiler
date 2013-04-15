package compiler.type;

import java.util.ArrayList;
import java.util.List;

import compiler.symbol.Symbol;

public class RECORD extends TYPE {

	public static final class RecordField {

		public TYPE type;
		public Symbol name;

		public RecordField(TYPE type, Symbol name) {
			this.type = type;
			this.name = name;
		}
	}

	public List<RecordField> fields;
	public Symbol name;

	public RECORD(Symbol name) {
		fields = new ArrayList<RecordField>();
		this.name = name;
	}

	public void addField(TYPE type, Symbol name) {
		fields.add(new RecordField(type, name));
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
