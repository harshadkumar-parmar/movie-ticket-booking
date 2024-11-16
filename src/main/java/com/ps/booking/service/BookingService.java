package com.ps.booking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ps.booking.dto.BookingDTO;
import com.ps.booking.entity.Booking;
import com.ps.booking.entity.BookingStatus;
import com.ps.booking.entity.Customer;
import com.ps.booking.entity.Seat;
import com.ps.booking.entity.SeatReservation;
import com.ps.booking.entity.Showtime;
import com.ps.booking.repository.BookingRepository;
import com.ps.booking.repository.SeatReservationRepository;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatReservationRepository seatReservationRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void makeBooking(String userId, BookingDTO bookingDTO) {
        
        for (int seat : bookingDTO.getSeatNumbers()) {
            String seatKey = "seat:" + seat;
            if(redisTemplate.hasKey(seatKey)) {
                String seatUserId = redisTemplate.opsForValue().get(seatKey);
                if(seatUserId != null && !seatUserId.equals(userId)) {
                    throw new RuntimeException("Seat already reserved");
                }
            }
        }
        
        Booking booking = new Booking();
        Customer user = new Customer();
        user.setId(Long.valueOf(userId));
        booking.setCustomer(user);
        Showtime showtime = new Showtime();
        showtime.setId(bookingDTO.getShowtimeId());
        booking.setShowtime(showtime);
        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setSeatReservations(new ArrayList<>());

        for(long seatId: bookingDTO.getSeatNumbers()) {
            String seatKey = "seat:" + seatId;
            redisTemplate.opsForValue().set(seatKey, userId);
            SeatReservation seatReservation = new SeatReservation();
            seatReservation.setBooking(booking);
            Seat seat = new Seat();
            seat.setId(seatId);
            seatReservation.setSeat(seat);
            seatReservation.setReservationDate(LocalDateTime.now());
            seatReservation.setReserved(true);
            booking.getSeatReservations().add(seatReservation);}

        bookingRepository.save(booking);
    }

    public void cancelBooking(Booking booking) {
        // Update the booking status to "CANCELLED"
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Update the seat reservation status to "CANCELLED"
        List<SeatReservation> seatReservations = seatReservationRepository.findByBooking(booking);
        for (SeatReservation seatReservation : seatReservations) {
            seatReservation.setReserved(false);
            seatReservationRepository.save(seatReservation);
        }
    }
}

