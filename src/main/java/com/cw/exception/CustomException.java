package com.cw.exception;

public class CustomException extends RuntimeException {
	private static final long serialVersionUID = -8321615902002052926L;

	public CustomException(String message) {
		super(message);
	}
}
