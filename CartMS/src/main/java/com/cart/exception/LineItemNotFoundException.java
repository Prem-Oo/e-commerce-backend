package com.cart.exception;

public class LineItemNotFoundException extends RuntimeException {

private static final long serialVersionUID = 1L;
	
	public LineItemNotFoundException(String msg) {
		super(msg);
	}
	
}
