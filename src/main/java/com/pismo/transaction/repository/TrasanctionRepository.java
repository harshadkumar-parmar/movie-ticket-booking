package com.pismo.transaction.repository;


import org.springframework.data.repository.CrudRepository;

import com.pismo.transaction.entity.Transaction;
import java.util.List;
import com.pismo.transaction.entity.Account;


public interface TrasanctionRepository extends CrudRepository<Transaction, Object> {
    List<Transaction> findByAccount(Account account);
}
