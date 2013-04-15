package compiler.absyn;

public class NameType extends TypeSpecifier {
	public TypedefName typedefName;

	public NameType(TypedefName typedefName) {
		super();
		this.typedefName = typedefName;
	}
}
