package compiler.absyn;

public class TypeName extends Expression {
	public TypeSpecifier type;
	public int starCount;

	public TypeName(TypeSpecifier type, int starCount) {
		super();
		this.type = type;
		this.starCount = starCount;
	}

}
