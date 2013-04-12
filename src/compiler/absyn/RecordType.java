package compiler.absyn;

import java.util.List;

public class RecordType extends TypeSpecifier {
	StructUnion structUnion;
	Id id;
	List<PlainDeclaration> plainDeclaration;

	public RecordType(StructUnion structUnion, Id id,
			List<PlainDeclaration> plainDeclaration) {
		super();
		this.structUnion = structUnion;
		this.id = id;
		this.plainDeclaration = plainDeclaration;
	}

}
