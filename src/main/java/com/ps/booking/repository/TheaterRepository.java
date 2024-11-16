package com.ps.booking.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.ps.booking.entity.Theater;

public interface TheaterRepository extends CrudRepository<Theater, Long> {

    List<Theater> findByCity(String city);

    // @Query("""
    //         SELECT t, t.screens, t.screens.screenshowtimes FROM Theater t
    //         WHERE t.city = :city
    //         AND t.screens.screenshowtimes.showtime.day = :day
    //         AND t.screens.screenshowtimes.showtime.movie.id = :movieId
    //         """)
    // List<TheaterWithShowtimesDto> searchByCityAndShowtimesAndMovie(String city, LocalDate day, Long movieId);
}
