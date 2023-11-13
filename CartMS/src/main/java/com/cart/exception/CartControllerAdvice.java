package com.cart.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class CartControllerAdvice extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@ExceptionHandler(CartNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleCustomerNotFoundException(CartNotFoundException e,HttpServletRequest r){
		System.out.println("InventoryNotFoundException....");
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(),e.getMessage(),"404",r.getRequestURI());
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(LineItemNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleLineItemNotFoundException(LineItemNotFoundException e,HttpServletRequest r){
		System.out.println("InventoryNotFoundException....");
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
