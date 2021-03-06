package de.prometheus.bildarchiv.util;

public class ProgressBar {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	private int ratio;
	private int i;
	private static boolean INTERRUPTED;

	public ProgressBar(int size) {
		this.ratio = size / 100;
		this.i = 0;
	}

	public void start() {
		System.out.print(ANSI_GREEN + "|");
	}

	public void increment(int incremential) {
		synchronized (this) {
			if (i >= ratio) {
				if(INTERRUPTED) {
					System.out.print("continueing ... " + ANSI_GREEN + "|");
					INTERRUPTED = false;
				}
				System.out.print("=");
				i = 0;
			}
			i += incremential;
		}
	}

	public void done() {
		System.out.println("| done!" + ANSI_RESET);
	}
	
	public static void error() {
		System.out.println(ANSI_RESET + ANSI_RED + " ... an error occured!" + ANSI_RESET);
		INTERRUPTED = true;
	}

}