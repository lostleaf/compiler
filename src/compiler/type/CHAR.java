package compiler.type;

import compiler.temp.IntConstant;

public final class CHAR extends TYPE {

	private static CHAR instance = null;

	public static synchronized CHAR getInstance() {
		if (instance == null) {
			instance = new CHAR();
		}
		return instance;
	}

	private CHAR() {
		size = new IntConstant(1);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof CHAR;
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
