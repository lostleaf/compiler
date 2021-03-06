package compiler.builder;

import compiler.type.ARRAY;
import compiler.type.POINTER;
import compiler.type.TYPE;

public class ArrayBuilder {

	public static TYPE build(TYPE t, int starCount, int exprCount) {
		for (int i = 0; i < starCount; i++)
			t = new POINTER(t);
		for (int i = 0; i < exprCount; i++)
			t = new ARRAY(t, 0);
		return  t;
	}

}
