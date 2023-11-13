package com.shopping.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class ShoppingControllerAdvice extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	
	// global exception handler
		@ExceptionHandler
		public ResponseEntity<ErrorDetails> allother(Exception e,HttpServletRequest r){
			System.out.println("Shopping-MS global exception handler...."+e.getMessage());
			System.out.println("Shopping-MS global exception ////...."+e);
			ErrorDetails err = new ErrorDetails(LocalDateTime.now(),e.getMessage(),"404",r.getRequestURI());
			return new ResponseEntity<ErrorDetails>(err, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

}
