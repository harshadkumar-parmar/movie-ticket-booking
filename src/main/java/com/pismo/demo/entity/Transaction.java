package com.pismo.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class Transaction {
    @Id
    @Column(name="transaction_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable=false)
    private Account account;

    @ManyToOne
    @JoinColumn(name="operation_type_id", nullable = false)
    private OperationType operationType;

    private BigDecimal amount;

    @Basic(optional = false)
    @Column(name = "event_date", nullable = false, insertable=false, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime eventDate;
}
