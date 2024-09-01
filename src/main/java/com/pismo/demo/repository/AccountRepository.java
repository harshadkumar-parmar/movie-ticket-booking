package com.pismo.demo.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import com.pismo.demo.entity.Account;


public interface  AccountRepository extends CrudRepository<Account, Object> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
}
