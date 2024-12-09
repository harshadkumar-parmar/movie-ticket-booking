package com.ps.booking.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Customer;
import com.ps.booking.entity.Transaction;


public interface TrasanctionRepository extends CrudRepository<Transaction, Object> {
    List<Transaction> findByCustomer(Customer customer);
}
