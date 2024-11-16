package com.ps.booking.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ps.booking.entity.Booking;
import com.ps.booking.entity.BookingStatus;
import com.ps.booking.repository.BookingRepository;

@Service
public class TicketReleaseService {
    @Autowired
    private BookingRepository bookingRepository;

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void releaseTickets() {
        // Get all bookings with a status of "PENDING"
        List<Booking> bookings = bookingRepository.findByBookingStatus(BookingStatus.PENDING);
        bookings.forEach(booking -> {
            booking.setBookingStatus(BookingStatus.EXPIRED);
            booking.getSeatReservations().forEach(seatReservation -> {
                seatReservation.setBooking(null);
                seatReservation.setReserved(false);
            });
        });
    
        // Save the changes to the database in bulk
        bookingRepository.saveAll(bookings);
    }
}