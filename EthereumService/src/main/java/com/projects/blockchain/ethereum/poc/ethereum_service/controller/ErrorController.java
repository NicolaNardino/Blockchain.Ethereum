package com.projects.blockchain.ethereum.poc.ethereum_service.controller;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.projects.blockchain.ethereum.utility.ErrorDescription;

@ControllerAdvice
@RestController
public class ErrorController extends ResponseEntityExceptionHandler {
  
  @ExceptionHandler(Throwable.class)
	public final ResponseEntity<ErrorDescription> handleAllExceptions(Throwable t, WebRequest request) {
	  final HttpHeaders headers = new HttpHeaders();
	  headers.setContentType(MediaType.APPLICATION_JSON);
	  return new ResponseEntity<>(new ErrorDescription(new Date(), t.getMessage(),request.getDescription(false)), headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
