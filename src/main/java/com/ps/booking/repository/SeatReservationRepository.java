package com.ps.booking.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Booking;
import com.ps.booking.entity.SeatReservation;

public interface SeatReservationRepository extends CrudRepository<SeatReservation, Object> {

    List<SeatReservation> findByBooking(Booking booking);

}
