package com.pismo.transaction.repository;

import org.springframework.data.repository.CrudRepository;

import com.pismo.transaction.entity.OperationType;

public interface OperationTypeRepository extends CrudRepository<OperationType, Object> {

}
