package com.mymes.equip.tc.auth.impl;

public class ExpiredRefreshTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ExpiredRefreshTokenException() {
		super();
	}
	
	public ExpiredRefreshTokenException(String message) {
		super(message);
	}
}
