package com.ps.booking.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    @NotNull(message = "Seat booking ID is required")
    private UUID bookingId;

    @NotNull(message = "Payment method ID is required")
    private UUID paymentMethodId;


    @NotNull(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code")
    private String currency;
}
