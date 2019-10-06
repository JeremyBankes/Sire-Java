package com.jeremy.sire;

public class Logger {

	private static boolean debug = false;

	public static void debug(String text, Object... args) {
		if (debug) {
			writeln(text, args);
		}
	}

	public static void info(String text, Object... args) {
		writeln(text, args);
	}

	public static void warn(String text, Object... args) {
		writeln(text, args);
	}

	public static void error(String text, Exception exception, Object... args) {
		writeln(text, args);
		writeln("%s: %s", exception.getClass().getSimpleName(), exception.getLocalizedMessage());
		for (StackTraceElement e : exception.getStackTrace()) {
			if (e.getFileName() != null) {
				String className = e.getClassName();
				writeln("\t> %s.%s(%s:%s)", className, e.getMethodName(), e.getFileName(), e.getLineNumber());
			}
		}
	}

	private static void writeln(String text, Object... args) {
		write(String.format(text, args));
		write('\n');
	}

	private static void write(Object text) {
		System.out.print(text);
	}

}
