package com.inventory.exception;

import java.time.LocalDateTime;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class InventoryControllerAdvice extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@ExceptionHandler(InventoryNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleCustomerNotFoundException(InventoryNotFoundException e,HttpServletRequest r){
		System.out.println("InventoryNotFoundException....");
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(),e.getMessage(),"404",r.getRequestURI());
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(OptimisticLockException.class)
	public ResponseEntity<String> handleOptimisticLockException(OptimisticLockException e) {
	    return ResponseEntity
	        .status(HttpStatus.CONFLICT)
	        .body("Optimistic locking conflict occurred: " + e.getMessage());
	}
	
	
	// global exception handler
		@ExceptionHandler
		public ResponseEntity<ErrorDetails> allother(Exception e,HttpServletRequest r){
			System.out.println("global exception handler....");
			ErrorDetails err = new ErrorDetails(LocalDateTime.now(),e.getMessage(),"404",r.getRequestURI());
			return new ResponseEntity<ErrorDetails>(err, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

}
