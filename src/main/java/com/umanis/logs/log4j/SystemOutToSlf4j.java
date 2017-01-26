package com.umanis.logs.log4j;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class SystemOutToSlf4j extends PrintStream {

	private static final PrintStream originalSystemOut = System.out;
	private static SystemOutToSlf4j systemOutToLogger;
	private StringBuffer mem;
	private ArrayList<Byte> bytes ;
	/**
	 * Enable forwarding System.out.println calls to the logger if the
	 * stacktrace contains the class parameter
	 * 
	 * @param clazz
	 */
	public static void enableForClass(Class clazz) {
		systemOutToLogger = new SystemOutToSlf4j(originalSystemOut,
				clazz.getName());
		System.setOut(systemOutToLogger);
	}

	/**
	 * Enable forwarding System.out.println calls to the logger if the
	 * stacktrace contains the package parameter
	 * 
	 * @param packageToLog
	 */
	public static void enableForPackage(String packageToLog) {
		systemOutToLogger = new SystemOutToSlf4j(originalSystemOut,
				packageToLog);
		System.setOut(systemOutToLogger);
	}

	/**
	 * Disable forwarding to the logger resetting the standard output to the
	 * console
	 */
	public static void disable() {
		System.setOut(originalSystemOut);
		systemOutToLogger = null;
	}

	private String packageOrClassToLog;

	private SystemOutToSlf4j(PrintStream original, String packageOrClassToLog) {
		super(original);
		this.packageOrClassToLog = packageOrClassToLog;
		this.mem = new StringBuffer();
		this.bytes = new ArrayList<Byte>(1024);
	}

	@Override
	public void flush() {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement caller = findCallerToLog(stack);
		if (caller == null) {
			super.flush();
			return;
		}
		org.apache.log4j.Logger logger = org.apache.log4j.Logger
				.getLogger(SystemOutToSlf4j.class);
		if (!"".equals(this.mem.toString())) {
			logger.info(mem.toString());
		}
		if (!this.bytes.isEmpty()) {
			Byte[] b = new Byte[this.bytes.size()];
			b = bytes.toArray(b);
			byte[] bb = new byte[this.bytes.size()];
			for (int i = 0; i < this.bytes.size(); i++) {
				bb[i] = this.bytes.get(i);
			}
			String output = new String(bb);
			logger.info(output);
		}
		this.mem = new StringBuffer();
		this.bytes = new ArrayList<Byte>(1024);
		super.flush();	
	};
	
	@Override
	public void print(char arg0) {
		 this.mem.append((char) arg0 );
	};
	
	@Override
	public void print(char[] arg0) {
		 this.mem.append(arg0);
	};
	
	@Override
	public void write(byte[] b) throws IOException {
		for (int i = 0; i <b.length; i++) {
			this.bytes.add(b[i]);
		}
		super.write(b);
	}
	
	@Override
	public void write(int b) {
		this.bytes.add((byte)b);
		super.write(b);
	}
	
	@Override
	public void write(byte[] buf, int off, int len) {
		for (int i = off; i <len; i++) {
			this.bytes.add(buf[i]);
		}
		super.write(buf, off, len);
	}
	
	@Override
	public void println(String line) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement caller = findCallerToLog(stack);
		if (caller == null) {
			super.println(line);
			return;
		}
		org.apache.log4j.Logger logger = org.apache.log4j.Logger
				.getLogger(caller.getClass());
		logger.info(line);
		// org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(caller
		// .getClass());
		// log.info(line);
	}
	
	@Override
	public void print(String line) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement caller = findCallerToLog(stack);
		if (caller == null) {
			super.println(line);
			return;
		}
		org.apache.log4j.Logger logger = org.apache.log4j.Logger
				.getLogger(caller.getClass());
		logger.info(line);
		// org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(caller
		// .getClass());
		// log.info(line);
	}
	public StackTraceElement findCallerToLog(StackTraceElement[] stack) {
		for (StackTraceElement element : stack) {
			if (element.getClassName().startsWith(packageOrClassToLog))
				return element;
		}

		return null;
	}

}