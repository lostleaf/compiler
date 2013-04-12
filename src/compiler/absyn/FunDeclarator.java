package compiler.absyn;

public class FunDeclarator extends Declarator {

	public Parameters para;

	public FunDeclarator(PlainDeclarator plainDeclarator, Parameters para) {
		super(plainDeclarator);
		this.para = para;
	}

}
