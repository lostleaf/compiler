package compiler.absyn;

import java.util.List;

import compiler.util.Pair;

public class RecordType extends TypeSpecifier {
	public StructUnion structUnion;
	public Id id;
	public List<Pair<TypeSpecifier, Declarators>> pairs;

	public RecordType(StructUnion structUnion, Id id,
			List<Pair<TypeSpecifier, Declarators>> pairs) {
		super();
		this.structUnion = structUnion;
		this.id = id;
		this.pairs = pairs;
	}

}
