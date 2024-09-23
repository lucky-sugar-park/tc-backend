package com.mymes.equip.vplc;

public class VirtualPlcException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public VirtualPlcException() {
		super();
	}
	
	public VirtualPlcException(String message) {
		super(message);
	}
	
	public VirtualPlcException(String message, Throwable cause) {
		super(message, cause);
	}
}
