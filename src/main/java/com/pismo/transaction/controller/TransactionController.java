package com.pismo.transaction.controller;

import java.util.List;

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

import com.pismo.transaction.dto.TransactionDto;
import com.pismo.transaction.entity.Transaction;
import com.pismo.transaction.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction", description = "Transaction Management API")
@AllArgsConstructor
@Slf4j
public class TransactionController {
    
    private final TransactionService transactionService;

    @Operation(summary = "Create Transaction", description = "API to create transaction", responses = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/")
    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<Transaction> createTransaction(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @Valid @RequestBody TransactionDto transaction) {
        log.trace("Create transaction request received for email: {} with transaction: {}", authentication.getName(), transaction);
        Transaction savedTransaction = transactionService.createTransaction(transaction, authentication.getName());
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/")
    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<List<Transaction>> getTransactions(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        log.trace("Get account request received for email: {}", authentication.getName());
        List<Transaction> transactions = transactionService.getTransactions(authentication.getName());
        return new ResponseEntity<>(transactions, HttpStatus.CREATED);
    }
}