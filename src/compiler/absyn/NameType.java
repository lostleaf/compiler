package compiler.absyn;

public class NameType extends TypeSpecifier {
	TypedefName typedefName;

	public NameType(TypedefName typedefName) {
		super();
		this.typedefName = typedefName;
	}
}
