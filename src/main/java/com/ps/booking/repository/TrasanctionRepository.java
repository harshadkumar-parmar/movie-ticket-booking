package com.ps.booking.repository;


import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Transaction;
import java.util.List;
import com.ps.booking.entity.Customer;


public interface TrasanctionRepository extends CrudRepository<Transaction, Object> {
    List<Transaction> findByAccount(Customer account);
}
