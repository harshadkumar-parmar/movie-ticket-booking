package com.ps.booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.booking.entity.Showtime;
import com.ps.booking.repository.ShowtimeRepository;

@Service
public class ShowService {

    @Autowired
    private ShowtimeRepository showRepository;

    public List<Showtime> findShows(Long[] theaterIds, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        System.out.println("--------------");
        return showRepository.findByTheaterIdInAndShowtimeBetween(theaterIds, startOfDay, endOfDay);
    }

    public List<Showtime> findShowsWithMovie(Long[] theaterIds, LocalDate date, Long movieId) {
        return showRepository.findByTheaterIdInAndMovieIdAndShowdate(theaterIds, movieId, date);
    }
}