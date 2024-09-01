package com.pismo.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pismo.demo.dto.TransactionDto;
import com.pismo.demo.entity.Transaction;
import com.pismo.demo.service.TransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction", description = "Transaction Management API")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/")
    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<Transaction> createTransaction(@CurrentSecurityContext(expression = "authentication") 
  Authentication authentication, @Valid @RequestBody TransactionDto transaction) {
        try {
            Transaction savedTransaction = transactionService.createTransaction(transaction, authentication.getName() );
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
         }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);   
        } 
    }

    @GetMapping("/")
    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<List<Transaction>> getTransactions(@CurrentSecurityContext(expression = "authentication") 
  Authentication authentication) {
        try {
            List<Transaction> transactions = transactionService.getTransactions(authentication.getName());
            return new ResponseEntity<>(transactions, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);   
        } 
    }
}