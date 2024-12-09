package com.ps.booking.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Showtime;
import com.ps.booking.entity.Theater;
public interface ShowtimeRepository extends CrudRepository<Showtime, Long> {
    List<Showtime> findByTheaterIdInAndShowtimeBetween(Long[] theaterIds, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Showtime> findByTheaterIdInAndMovieIdAndShowdate(Long[] theaterIds, Long movieId,
            LocalDate startOfDay);

    public List<Showtime> findByTheaterAndShowdate(Theater theater, LocalDate chosenDate);
}
