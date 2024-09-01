package com.pismo.demo.repository;


import org.springframework.data.repository.CrudRepository;

import com.pismo.demo.entity.Transaction;
import java.util.List;
import com.pismo.demo.entity.Account;


public interface TrasanctionRepository extends CrudRepository<Transaction, Object> {
    List<Transaction> findByAccount(Account account);
}
