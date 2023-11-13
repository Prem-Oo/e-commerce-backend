package com.customer.exception;

public class CustomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CustomerNotFoundException(String msg) {
		super(msg);
	}
	
	
	

}
