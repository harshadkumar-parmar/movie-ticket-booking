package com.pismo.transaction.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.pismo.transaction.dto.TransactionDto;
import com.pismo.transaction.entity.Account;
import com.pismo.transaction.entity.OperationType;
import com.pismo.transaction.entity.Transaction;
import com.pismo.transaction.service.TransactionService;

import jdk.jfr.Description;

@SpringBootTest
@Description("Tests for TransactionController")
@ActiveProfiles("test")
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    Account account;
    OperationType operationType;
    Transaction transaction;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("testUser", "testPassword"));
        account = new Account();
        operationType = new OperationType();
        transaction = new Transaction();

        account.setAccountId(1L);
        account.setDocumentNumber("123");
        account.setEmail("a@b.com");
        account.setName("Joe");

        operationType.setOperationTypeId((short) 1);
        operationType.setDescription("Deposit");
        operationType.setNegative(true);
        
        transaction.setTransactionId(1L);
        transaction.setAccount(account);
        transaction.setOperationType(operationType);
        transaction.setAmount(BigDecimal.valueOf(100.0));
    }

    @Test
    public void testCreateTransaction_PositiveScenario() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setOperationTypeId((short) 1);
        transactionDto.setAmount(BigDecimal.valueOf(100));

        

        when(transactionService.createTransaction(any(TransactionDto.class), any(String.class))).thenReturn(transaction);
        ResponseEntity<Transaction> response = transactionController.createTransaction(
            SecurityContextHolder.getContext().getAuthentication(), transactionDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    @Test
    public void testCreateTransaction_NegativeScenario_InvalidInput() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(null);
         
            transactionController.createTransaction(SecurityContextHolder.getContext().getAuthentication(), transactionDto);
        // assertEquals("amount cannot be null", ex.getMessage());
    }

    @Test
    public void testCreateTransaction_NegativeScenario_InternalServerError() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setOperationTypeId((short) 1);
        transactionDto.setAmount(BigDecimal.valueOf(100));

        when(transactionService.createTransaction(any(TransactionDto.class), any(String.class))).thenThrow(new RuntimeException());

        // Act and Assert
        try {
            transactionController.createTransaction(
                SecurityContextHolder.getContext().getAuthentication(),
                transactionDto);
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
        }
    }

    @Test
    public void testGetTransactions_PositiveScenario() {
        when(transactionService.getTransactions(any(String.class))).thenReturn(List.of(transaction));

        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getTransactions(
            SecurityContextHolder.getContext().getAuthentication()
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testGetTransactions_NegativeScenario_NoTransactionsFound() {
        // Arrange
        when(transactionService.getTransactions(any(String.class))).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getTransactions(
            SecurityContextHolder.getContext().getAuthentication()
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody());
    }
}