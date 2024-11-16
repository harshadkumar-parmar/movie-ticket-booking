package com.ps.booking.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Booking {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Showtime showtime;

    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private double totalAmount;
    private double taxAmount;
    private double discountAmount;

    @Enumerated(EnumType.STRING)
    private BookingPaymentStatus paymentStatus;

    @OneToMany(mappedBy = "booking")
    private List<SeatReservation> seatReservations;
}