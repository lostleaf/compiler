package compiler.util;

public final class Error {

	public static boolean exitOnFatal = false;

	private String message = null;

	public Error(String str) {
		this(str, false);
	}

	public Error(String str, boolean silent) {
		message = str;

		if (!silent) {
			System.err.println(this);
			System.err.flush();
		}
	}

	public String toString() {
		return message;
	}
}
