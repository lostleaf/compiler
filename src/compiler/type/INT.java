package compiler.type;

import compiler.temp.IntConstant;

public final class INT extends TYPE {

	private static INT instance = null;

	public static synchronized INT getInstance() {
		if (instance == null) {
			instance = new INT();
		}
		return instance;
	}

	private INT() {
		size = new IntConstant(4);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof INT;
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
