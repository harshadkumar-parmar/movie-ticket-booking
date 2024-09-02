package com.pismo.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pismo.demo.dto.TransactionDto;
import com.pismo.demo.entity.Account;
import com.pismo.demo.entity.OperationType;
import com.pismo.demo.entity.Transaction;
import com.pismo.demo.repository.OperationTypeRepository;
import com.pismo.demo.repository.TrasanctionRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class TransactionService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @Autowired
    private TrasanctionRepository transactionRepository;

    /**
     * Creates a new transaction
     * @param TransactionDto the transaction to create
     * @param email the email of the account that the transaction belongs to
     * @return the created transaction
     * @throws RuntimeException if the operation type is not found
     */
    @Transactional(TxType.REQUIRED)
    public Transaction createTransaction(TransactionDto transaction, String email) {
        Account account = accountService.geAccountByEmail(email);
        Optional<OperationType> operationTypeOptional = operationTypeRepository.findById(transaction.getOperationTypeId());
        if(operationTypeOptional.isEmpty()) {
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
        return transactionRepository.save(t);
    }

    /**
     * Retrieves all transactions of a given account
     * @param email the email of the account
     * @return a list of transactions
     */
    public List<Transaction> getTransactions(String email) {
        Account account = accountService.geAccountByEmail(email);
        return transactionRepository.findByAccount(account);
    }
}