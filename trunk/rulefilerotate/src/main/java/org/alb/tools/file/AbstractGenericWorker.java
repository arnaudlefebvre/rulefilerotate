package org.alb.tools.file;

public abstract class AbstractGenericWorker {

	public AbstractGenericWorker() {

	}

	public abstract Boolean configure(String[] args) throws Exception;

	public abstract Boolean run() throws Exception;

}
