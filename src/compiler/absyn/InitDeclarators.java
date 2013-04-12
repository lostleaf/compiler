package compiler.absyn;

import java.util.List;

public class InitDeclarators extends Node {
	public List<InitDeclarator> initDecl;

	public InitDeclarators(List<InitDeclarator> id) {
		super();
		this.initDecl = id;
	}
}
