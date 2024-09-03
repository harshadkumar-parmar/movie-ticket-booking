package com.pismo.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pismo.transaction.dto.TransactionDto;
import com.pismo.transaction.entity.Account;
import com.pismo.transaction.entity.OperationType;
import com.pismo.transaction.entity.Transaction;
import com.pismo.transaction.repository.OperationTypeRepository;
import com.pismo.transaction.repository.TrasanctionRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    
    private final AccountService accountService;
    private final OperationTypeRepository operationTypeRepository;
    private final TrasanctionRepository transactionRepository;

    /**
     * Creates a new transaction
     * @param TransactionDto the transaction to create
     * @param email the email of the account that the transaction belongs to
     * @return the created transaction
     * @throws RuntimeException if the operation type is not found
     */
    @Transactional(TxType.REQUIRED)
    public Transaction createTransaction(TransactionDto transaction, String email) {
        log.trace("Creating transaction for email: {} with transaction: {}", email, transaction);
        Account account = accountService.geAccountByEmail(email);

        // check if operation type exists
        Optional<OperationType> operationTypeOptional = operationTypeRepository.findById(transaction.getOperationTypeId());
        log.trace("Operation type: {}", operationTypeOptional);
        if(operationTypeOptional.isEmpty()) {
            log.error("Operation type not found");
            throw new RuntimeException("Operation type not found");
        }
        OperationType operationType = operationTypeOptional.get();
        Transaction t = new Transaction();
        t.setAccount(account);
        t.setEventDate(LocalDateTime.now());
        t.setOperationType(operationType);
        BigDecimal amount = transaction.getAmount();
        if(operationType.isNegative()) {
            amount = amount.negate();
        }
        t.setAmount(amount);
        log.trace("Transaction created: {}", t);
        return transactionRepository.save(t);
    }

    /**
     * Retrieves all transactions of a given account
     * @param email the email of the account
     * @return a list of transactions
     */
    public List<Transaction> getTransactions(String email) {
        log.trace("Fetching transactions for email: {}", email);
        Account account = accountService.geAccountByEmail(email);
        log.trace("Fetching transactions for account: {}", account);
        return transactionRepository.findByAccount(account);
    }
}