package com.pismo.transaction.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.pismo.transaction.controller")
public class ValidationExceptionHandler {


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
}
