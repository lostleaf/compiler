package compiler.type;

import compiler.temp.IntConstant;

public class VOID extends TYPE {

	private VOID() {
		size = new IntConstant(0);
	}

	static private VOID instance = null;

	public static VOID getInstance() {
		if (instance == null)
			instance = new VOID();
		return instance;
	}

	@Override
	public boolean equals(Object o) {
		return o == instance;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public boolean isRecord() {
		return false;
	}

}
