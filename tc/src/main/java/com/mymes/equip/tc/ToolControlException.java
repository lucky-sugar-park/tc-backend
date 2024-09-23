package com.mymes.equip.tc;

public class ToolControlException extends Exception {

	private static final long serialVersionUID = 1L;

	public ToolControlException() {
		super();
	}
	
	public ToolControlException(String message) {
		super(message);
	}
	
	public ToolControlException(Throwable th) {
		super(th);
	}
}
