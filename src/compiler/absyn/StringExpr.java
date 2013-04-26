package compiler.absyn;

public class StringExpr extends Expression {
	public String value;
	public StringExpr(String val){
		value = val;
	}
}
