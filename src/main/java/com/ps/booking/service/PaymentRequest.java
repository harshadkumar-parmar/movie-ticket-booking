package com.ps.booking.service;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String requestId;
    private String paymentMethodId;
    private double amount;
    private double taxAmount;
    private double discountAmount;
    private String description;
    private String returnUrl;
    private String cancelUrl;
    private Date paymentDate;
    private long bookingId;
}