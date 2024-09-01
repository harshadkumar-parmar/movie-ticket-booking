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
import com.pismo.demo.repository.AccountRepository;
import com.pismo.demo.repository.OperationTypeRepository;
import com.pismo.demo.repository.TrasanctionRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @Autowired
    private TrasanctionRepository transactionRepository;

    /**
     * Creates a new transaction
     * @param transaction the transaction to create
     * @return the created transaction
     */
    @Transactional(TxType.REQUIRED)
    public Transaction createTransaction(TransactionDto transaction, String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow();
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

    public List<Transaction> getTransactions(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow();
        return transactionRepository.findByAccount(account);
    }
}