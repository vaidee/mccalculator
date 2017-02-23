package com.mediacorp.exception;

public class McExceptions extends Exception {

	String message = null;

	public McExceptions(String ErrorMsg) {
		super(ErrorMsg);
		message = ErrorMsg;
	}

	public String getErrorMessage() {
		return super.getMessage();
	}

}
