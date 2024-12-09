package com.ps.booking.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps.booking.dto.TheaterWithShowtimesDto;
import com.ps.booking.entity.Showtime;
import com.ps.booking.entity.Theater;
import com.ps.booking.repository.ShowtimeRepository;
import com.ps.booking.repository.TheaterRepository;

@RestController
@RequestMapping("/api/v1/theaters")
public class TheaterController {

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    /**
     * Get all theaters with showtimes for a given city and date.
     * 
     * @param city    the city to search in
     * @param lat     the latitude of the location to calculate the distance from
     * @param lon     the longitude of the location to calculate the distance from
     * @param date    the date to search for
     * @return        a list of TheaterWithShowtimesDto, each containing a theater and its
     *                corresponding showtimes for the given date, sorted by distance from the
     *                given location
     */
    @GetMapping()
    public List<TheaterWithShowtimesDto> allTheaters(
            @RequestParam("city") String city,
            @RequestParam("lat") Double lat,
            @RequestParam("long") Double lon,
            @RequestParam("date") LocalDate chosenDate) {
        List<Theater> theaters = theaterRepository.findByCity(city);

        List<TheaterWithShowtimesDto> theaterWithShowtimesDtos = new ArrayList<>();

        for (Theater theater : theaters) {
            List<Showtime> showtimes = showtimeRepository.findByTheaterAndShowdate(theater,chosenDate);
            if (!showtimes.isEmpty()) {
                TheaterWithShowtimesDto theaterWithShowtimesDto = new TheaterWithShowtimesDto(theater);
                theaterWithShowtimesDto.setShowtimes(showtimes);
                theaterWithShowtimesDto.setDistance(calculateDistance(lat, lon, theater.getLatitude(), theater.getLongitude()));
                theaterWithShowtimesDtos.add(theaterWithShowtimesDto);
            }
        }
        return theaterWithShowtimesDtos;
    }

    /**
     * Calculates the distance between two points on the surface of a sphere (e.g. the Earth)
     * using the Haversine formula.
     * 
     * @param lat1 the latitude of the first point
     * @param lon1 the longitude of the first point
     * @param lat2 the latitude of the second point
     * @param lon2 the longitude of the second point
     * @return the distance between the two points in kilometers
     */
    private Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double earthRadius = 6371.0; // in kilometers
        Double dLat = Math.toRadians(lat2 - lat1);
        Double dLon = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = earthRadius * c;
        return Math.round(distance * 100D) / 100D;
    }
}
