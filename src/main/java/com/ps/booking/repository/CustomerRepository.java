package com.ps.booking.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Customer;


public interface  CustomerRepository extends CrudRepository<Customer, Object> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
}
