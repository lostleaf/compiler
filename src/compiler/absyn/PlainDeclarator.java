package compiler.absyn;

public class PlainDeclarator extends Node {
	public int starCount;
	public Id id;

	public PlainDeclarator(int starCount, Id id) {
		super();
		this.starCount = starCount;
		this.id = id;
	}

}
