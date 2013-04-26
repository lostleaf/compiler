package compiler.type;

public class FUNCTION extends TYPE {
	public TYPE argumentType;
	public TYPE returnType;
	public boolean defined, varyingArgument;

	public FUNCTION(TYPE argumentType, TYPE returnType, boolean defined,
			boolean varyingArgument) {
		this.argumentType = argumentType;
		this.returnType = returnType;
		this.defined = defined;
		this.varyingArgument = varyingArgument;
	}

	public TYPE getFinalReturnType() {
		if (returnType instanceof FUNCTION)
			return ((FUNCTION) returnType).getFinalReturnType();
		return returnType;
	}

	@Override
	public boolean isArray() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRecord() {
		// TODO Auto-generated method stub
		return false;
	}

}
