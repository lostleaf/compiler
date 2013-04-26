package compiler.absyn;

public class TypeName extends Expression {
	public TypeSpecifier typeSpecifier;
	public int starCount;

	public TypeName(TypeSpecifier type, int starCount) {
		//super();
		this.typeSpecifier = type;
		this.starCount = starCount;
	}

}
