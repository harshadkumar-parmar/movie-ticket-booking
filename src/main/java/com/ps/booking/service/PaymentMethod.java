package com.ps.booking.service;

import lombok.Data;

@Data
public class PaymentMethod {
    private String paymentMethodId;
    private String paymentMethodName;
    private String paymentMethodDescription;
    private String paymentMethodIcon;
    private boolean isDefault;
}