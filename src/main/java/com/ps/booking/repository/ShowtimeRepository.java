package com.ps.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Showtime;
import com.ps.booking.entity.Theater;
public interface ShowtimeRepository extends CrudRepository<Showtime, Long> {
    List<Showtime> findByTheaterIdInAndShowtimeBetween(Long[] theaterIds, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Showtime> findByTheaterIdInAndMovieIdAndShowtimeBetween(Long[] theaterIds, Long movieId,
            LocalDateTime startOfDay, LocalDateTime endOfDay);

    public List<Showtime> findByTheaterAndShowDate(Theater theater, String chosenDate);
}
