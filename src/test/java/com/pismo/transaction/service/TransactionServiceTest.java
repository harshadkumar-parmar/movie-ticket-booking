package com.pismo.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

import com.pismo.transaction.dto.TransactionDto;
import com.pismo.transaction.entity.Account;
import com.pismo.transaction.entity.OperationType;
import com.pismo.transaction.entity.Transaction;
import com.pismo.transaction.repository.OperationTypeRepository;
import com.pismo.transaction.repository.TrasanctionRepository;

import jdk.jfr.Description;

@SpringBootTest
public class TransactionServiceTest {

    @Mock
    private OperationTypeRepository operationTypeRepository;

    @Mock
    private TrasanctionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    private Account account;
    private OperationType operationTypePurchase;
    private OperationType operationTypeDeposite;
    private TransactionDto transactionDto;


    @BeforeEach
    public void setup() {
        account = new Account();
        account.setAccountId(1L);
        account.setEmail("john.doe@example.com");
        account.setName("John");
        account.setDocumentNumber("1234567890");
        account.setPassword("12345678");

        transactionDto = new TransactionDto();
        transactionDto.setOperationTypeId((short)1);
        transactionDto.setAmount(BigDecimal.valueOf(100.0));

        operationTypePurchase = new OperationType();
        operationTypePurchase.setOperationTypeId((short) 1L);
        operationTypePurchase.setDescription("PURCHASE");
        operationTypePurchase.setNegative(true);

        operationTypeDeposite = new OperationType();
        operationTypeDeposite.setOperationTypeId((short) 2L);
        operationTypeDeposite.setDescription("DEPOSIT");
        operationTypeDeposite.setNegative(false);
    }

    @Test
    @Description("Should create a transaction")
    public void shouldCreateTransaction() {

        Transaction t = new Transaction();
        t.setAccount(account);
        t.setEventDate(LocalDateTime.now());
        t.setOperationType(operationTypePurchase);
        t.setAmount(BigDecimal.valueOf(-100.0));
        t.setTransactionId(1L);

        when(accountService.geAccountByEmail("john.doe@example.com")).thenReturn(account);
        when(operationTypeRepository.findById((short) 1)).thenReturn(Optional.of(operationTypePurchase));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(t);

        Transaction result = transactionService.createTransaction(transactionDto, "john.doe@example.com");

        assertEquals(t, result);
        assertEquals(operationTypePurchase, result.getOperationType());
        assertEquals(account, result.getAccount());
        assertEquals(BigDecimal.valueOf(-100.0), result.getAmount());
    }

    @Test
    @Description("Should throw exception when operation type not found")
    public void shouldThrowExceptionWhenOperationTypeNotFound() {
        when(accountService.geAccountByEmail("john.doe@example.com")).thenReturn(account);
        when(operationTypeRepository.findById((short) 1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, 
            () -> transactionService.createTransaction(transactionDto, "john.doe@example.com"));

        assertEquals("Operation type not found", exception.getMessage());
    }

    @Test
    @Description("Should return positive transaction when operation type is purchase")
    public void shouldReturnPositiveTransactionWhenOperationTypeIsDeposit() {
        Transaction t = new Transaction();
        t.setAccount(account);
        t.setEventDate(LocalDateTime.now());
        t.setOperationType(operationTypePurchase);
        t.setAmount(BigDecimal.valueOf(100.0));
        t.setTransactionId(1L);

        when(accountService.geAccountByEmail("john.doe@example.com")).thenReturn(account);
        when(operationTypeRepository.findById((short) 2)).thenReturn(Optional.of(operationTypeDeposite));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(t);
        transactionDto.setOperationTypeId((short) 2);
        Transaction result = transactionService.createTransaction(transactionDto, "john.doe@example.com");

        assertEquals(t, result);
        assertEquals(operationTypePurchase, result.getOperationType());
        assertEquals(account, result.getAccount());
        assertEquals(BigDecimal.valueOf(100.0), result.getAmount());
    }

    @Test
    @Description("Should get transactions")
    public void shouldGetTransactions() {
        when(accountService.geAccountByEmail("john.doe@example.com")).thenReturn(account);
        when(transactionRepository.findByAccount(account)).thenReturn(java.util.Arrays.asList(new Transaction()));
        java.util.List<Transaction> result = transactionService.getTransactions("john.doe@example.com");
        assertEquals(1, result.size());
    }
}