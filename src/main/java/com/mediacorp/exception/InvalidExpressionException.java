package com.mediacorp.exception;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InvalidExpressionException extends WebApplicationException{
	
	public InvalidExpressionException(String message) {
		super(message);
		System.out.println("Exception occured");

	}

}
