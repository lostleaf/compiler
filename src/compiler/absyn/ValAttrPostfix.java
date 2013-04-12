package compiler.absyn;

public class ValAttrPostfix extends Postfix {
	public Id identifier = null;

	public ValAttrPostfix(Id identifier) {
		super();
		this.identifier = identifier;
	}
}
