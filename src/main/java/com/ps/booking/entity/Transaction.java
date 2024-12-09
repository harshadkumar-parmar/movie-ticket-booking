package com.ps.booking.entity;

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
    @JoinColumn(name = "customer", nullable=false)
    private Customer customer;

    private BigDecimal amount;

    @Basic(optional = false)
    @Column(name = "event_date", nullable = false, insertable=false, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime eventDate;
}
