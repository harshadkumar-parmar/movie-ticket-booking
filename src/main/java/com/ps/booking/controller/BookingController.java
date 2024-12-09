package com.ps.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps.booking.dto.BookingDTO;
import com.ps.booking.entity.Booking;
import com.ps.booking.service.BookingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/reserve")
    public Booking reserveSeats(@RequestParam String userId, @RequestBody BookingDTO bookingDTO) throws Exception {
        Booking booking = bookingService.reserveSeats(userId, bookingDTO);
        return booking;
    }

    @PostMapping("/confirm")
    public void confirmSeats(@RequestParam String userId, @RequestParam Long bookingId) throws Exception {
        bookingService.confirmSeats(userId, bookingId);
    }
}
