package com.ps.booking.repository;

import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Seat;

public interface SeatRepository extends CrudRepository<Seat, Long> {}
