package com.ps.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ps.booking.entity.Booking;
import com.ps.booking.entity.BookingStatus;
import com.ps.booking.entity.SeatReservation;
import com.ps.booking.exception.PaymentException;
import com.ps.booking.repository.BookingRepository;
import com.ps.booking.repository.SeatReservationRepository;

public class PaymentService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatReservationRepository seatReservationRepository;

    @Autowired
    private PaymentProcessor paymentGateway;

    public void makePayment(Booking booking, PaymentMethod paymentMethod) {
        try {

            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setBookingId(booking.getId());
            paymentRequest.setPaymentMethodId(paymentMethod.getPaymentMethodId());
            paymentRequest.setAmount(booking.getTotalAmount());
            paymentRequest.setDescription("Payment for " + booking.getShowtime().getMovie().getTitle() + " at " + booking.getShowtime().getScreen().getTheater().getName());

            PaymentResult paymentResult = paymentGateway.processPayment(paymentRequest);

            if (paymentResult.isSuccess()) {
                // Update the booking status to "CONFIRMED"
                booking.setBookingStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);

                // Update the seat reservation status to "RESERVED"
                List<SeatReservation> seatReservations = seatReservationRepository.findByBooking(booking);
                for (SeatReservation seatReservation : seatReservations) {
                    seatReservation.setReserved(true);
                    seatReservationRepository.save(seatReservation);
                }
            } else {
                // Update the booking status to "FAILED"
                booking.setBookingStatus(BookingStatus.FAILED);
                bookingRepository.save(booking);

                // Release the seats
                List<SeatReservation> seatReservations = seatReservationRepository.findByBooking(booking);
                for (SeatReservation seatReservation : seatReservations) {
                    seatReservation.setReserved(false);
                    seatReservationRepository.save(seatReservation);
                }
            }
        } catch (PaymentException e) {
            // Handle payment exceptions
            // Update the booking status to "FAILED"
            booking.setBookingStatus(BookingStatus.FAILED);
            bookingRepository.save(booking);

            // Release the seats
            List<SeatReservation> seatReservations = seatReservationRepository.findByBooking(booking);
            for (SeatReservation seatReservation : seatReservations) {
                seatReservation.setReserved(false);
                seatReservationRepository.save(seatReservation);
            }
        }
    }

}
