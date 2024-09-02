package com.pismo.transaction.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class OperationType {
    @NotNull
    @Id
    @Column(name = "operation_type_id", nullable = false)
    private short operationTypeId;

    @NotNull
    private String description;

    @NotNull
    @Column(nullable = false, name = "is_negative")
    private boolean isNegative;
    
    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
