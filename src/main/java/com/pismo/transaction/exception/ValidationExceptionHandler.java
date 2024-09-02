package com.pismo.transaction.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ValidationExceptionHandler {

  /**
   * Handles ResourceNotFoundException. Returns a JSON response with
   * status code 404 containing a map with the key "timestamp", "status", "error", "message", and "path"
   * as the value.
   * 
   * @param ex     the exception to handle
   * @param request the current request
   * @return a ResponseEntity containing the error map
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
        HttpStatus.NOT_FOUND.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    
    return message;
  }


  /**
   * Handles MethodArgumentNotValidException. Returns a JSON response with
   * status code 400 containing a map with the key "errors" and a list of strings
   * as the value. Each string is the default message of the exceptions.
   * 
   * @param ex     the exception to handle
   * @param request the current request
   * @return a ResponseEntity containing the error map
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> errors = new ArrayList<>();

    ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

    Map<String, List<String>> result = new HashMap<>();
    result.put("errors", errors);

    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  
  /**
   * Handles any other exceptions. Returns a JSON response with status code
   * 500 containing a map with the key "error" and a string as the value. The
   * string is the default message of the exception.
   * 
   * @param ex     the exception to handle
   * @param request the current request
   * @return a ResponseEntity containing the error map
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    
    return message;
  }
}
