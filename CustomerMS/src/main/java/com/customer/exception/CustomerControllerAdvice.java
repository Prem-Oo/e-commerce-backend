package com.customer.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class CustomerControllerAdvice extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleCustomerNotFoundException(CustomerNotFoundException e,HttpServletRequest r){
		System.out.println("CustomerNotFoundException....");
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(),e.getMessage(),"404",r.getRequestURI());
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);
		
	}
	
	
	// global exception handler
		@ExceptionHandler
		public ResponseEntity<ErrorDetails> allother(Exception e,HttpServletRequest r){
			System.out.println("global exception handler....");
			ErrorDetails err = new ErrorDetails(LocalDateTime.now(),e.getMessage(),"404",r.getRequestURI());
			return new ResponseEntity<ErrorDetails>(err, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

}
