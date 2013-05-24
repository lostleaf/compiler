package compiler.type;

import java.util.ArrayList;
import java.util.List;

import compiler.symbol.Symbol;
import compiler.temp.Addr;

public class RECORD extends TYPE {

	public static final class RecordField {

		public TYPE type;
		public Symbol name;
		public Addr offset;

		public RecordField(TYPE type, Symbol name, Addr offset) {
			this.type = type;
			this.name = name;
			this.offset = offset;
		}

	}

	public List<RecordField> fields;
	public Symbol name;

	public RECORD(Symbol name) {
		fields = new ArrayList<RecordField>();
		this.name = name;
	}

	public boolean hasField(Symbol name) {
		for (RecordField rf : fields)
			if (name.equals(rf.name))
				return true;
		return false;
	}

	public void addField(TYPE type, Symbol name, Addr offset) {
		fields.add(new RecordField(type, name, offset));
	}

	public RecordField findField(Symbol name) {
		for (RecordField rf : fields)
			if (name.equals(rf.name))
				return rf;
		return null;
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
