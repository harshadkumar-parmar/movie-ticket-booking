package com.ps.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps.booking.service.RedisService;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private RedisService redisService;

    private static final long RESERVATION_TTL = 60 * 15; // 5 minutes TTL for seat hold and will change to admin configure in future

    @PostMapping("/")
    public String reserveSeat(@RequestParam String showId, @RequestParam Long[] seatNumbers) {

        if (redisService.areSeatsHold(showId, seatNumbers)) {
            return "Seat is already booked.";
        }
        redisService.holdSeats(showId, seatNumbers, RESERVATION_TTL);
        return "Seat reserved successfully.";
    }
}
