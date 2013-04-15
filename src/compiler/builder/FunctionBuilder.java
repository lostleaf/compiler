package compiler.builder;

import java.util.List;

import compiler.symbol.Symbol;
import compiler.type.FUNCTION;
import compiler.type.POINTER;
import compiler.type.TYPE;
import compiler.util.Pair;

public class FunctionBuilder {
	public static FUNCTION build(TYPE retnType, int starCount,
			List<Pair<TYPE, Symbol>> params, boolean isVarparam, boolean isDef) {
		for (int i = 0; i < starCount; i++)
			retnType = new POINTER(retnType);

		if (params == null || params.size() == 0)
			return new FUNCTION(null, retnType, isVarparam, isDef);

		for (int i = params.size() - 1; i >= 0; --i) {
			retnType = new FUNCTION(params.get(i).first, retnType, isVarparam,
					isDef);
		}
		return (FUNCTION) retnType;
	}
}
