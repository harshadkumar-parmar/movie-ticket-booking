package com.ps.booking.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Booking;
import com.ps.booking.entity.BookingStatus;

public interface BookingRepository extends CrudRepository<Booking, Long> {

    List<Booking> findByBookingStatus(BookingStatus status);

}
