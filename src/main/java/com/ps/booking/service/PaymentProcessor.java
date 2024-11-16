package com.ps.booking.service;

import java.util.Random;

import com.ps.booking.exception.PaymentException;


public class PaymentProcessor {
    public PaymentResult processPayment(PaymentRequest paymentRequest) throws PaymentException {
        Random random = new Random();
        boolean paymentSucceeded = random.nextBoolean();

        if (paymentSucceeded) {
            return new PaymentResult(true, "Payment successful");
        } else {
            return new PaymentResult(false, "Payment failed");
        }
    }
}
