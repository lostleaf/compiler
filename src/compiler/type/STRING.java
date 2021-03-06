package compiler.type;

public final class STRING extends TYPE {

	private static STRING instance = null;

	public static synchronized STRING getInstance() {
		if (instance == null) {
			instance = new STRING();
		}
		return instance;
	}

	private STRING() {
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof STRING;
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
