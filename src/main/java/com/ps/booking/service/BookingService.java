package com.ps.booking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.booking.dto.BookingDTO;
import com.ps.booking.entity.Booking;
import com.ps.booking.entity.BookingStatus;
import com.ps.booking.entity.Customer;
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
    private RedissonClient redissonClient;

    public Booking reserveSeats(String userId, BookingDTO bookingDTO) throws Exception {
        RLock[] locks = new RLock[bookingDTO.getSeatNumbers().length];
        for (int idx = 0; idx < bookingDTO.getSeatNumbers().length; idx++) {
            int seatNum = bookingDTO.getSeatNumbers()[idx];
            locks[idx] = redissonClient.getLock("seatlock:"+bookingDTO.getShowtimeId()+":"+seatNum);
        }
        boolean isLocked = false;

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

        try {
            for (RLock lock : locks) {
                if(!lock.tryLock(15, TimeUnit.MINUTES)){
                    throw new Exception("Could not acquire lock to reserve seats.");
                }
            }
            isLocked = true;
            if (isLocked) {
                for (int seatId : bookingDTO.getSeatNumbers()) {
                    SeatReservation seat = seatReservationRepository.findById(seatId).orElseThrow();
                    if (!seat.isReserved()) {
                        seat.setReserved(true);
                        seat.setBooking(booking);
                        seat.setReservationDate(LocalDateTime.now());
                        seat.setExpirationTime(LocalDateTime.now().plusMinutes(15));
                        seatReservationRepository.save(seat);
                        booking.getSeatReservations().add(seat);
                        System.out.println("Seat " + seat.getSeat().getSeatNumber() + " reserved for user " + userId + ".");
                    } else {
                        System.out.println("Seat " + seat.getSeat().getSeatNumber() + " is already reserved.");
                    }
                }
            }
            return bookingRepository.save(booking);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Exception("Failed to acquire lock due to interruption.");
        } finally {
            if (isLocked) {
                for (RLock lock: locks) {
                    lock.unlock();
                }
            }
        }
    }

    public Booking confirmSeats(String userId, long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        RLock[] locks = new RLock[booking.getSeatReservations().size()];
        for (int idx = 0; idx < booking.getSeatReservations().size(); idx++) {
            SeatReservation seatNum = booking.getSeatReservations().get(idx);
            locks[idx] = redissonClient.getLock("seatlock:" + booking.getShowtime().getId() + ":" + seatNum.getSeat().getSeatNumber());
        }
        boolean isLocked = false;
    
        try {
            for (RLock lock : locks) {
                if (!lock.tryLock(15, TimeUnit.MINUTES)) {
                    throw new Exception("Could not acquire lock to confirm seats.");
                }
            }
            isLocked = true;
            if (isLocked) {
                for (SeatReservation seat : booking.getSeatReservations()) {
                    if (seat.isReserved() && seat.getBooking().getCustomer().getId().equals(userId)) {
                        seat.setReserved(true);
                        seat.setExpirationTime(null);
                        seat.setConfirmed(true);
                        seatReservationRepository.save(seat);
                        System.out.println("Seat " + seat.getSeat().getSeatNumber() + " confirmed for user " + userId + ".");
                    } else {
                        throw new Exception("No reservation found for the seat " + seat.getSeat().getSeatNumber() + " for user " + userId + ".");
                    }
                }
            }
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            return bookingRepository.save(booking);
        } finally {
            if (isLocked) {
                for (RLock lock : locks) {
                    lock.unlock();
                }
            }
        }
    }
    
}

