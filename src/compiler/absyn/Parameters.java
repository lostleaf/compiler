package compiler.absyn;

import java.util.List;

public class Parameters extends Node {
	public List<PlainDeclaration> list = null;
	public boolean varparams;

	public Parameters(List<PlainDeclaration> list, boolean b) {
		this.list = list;
		this.varparams = b;
	}
}
