package com.ps.booking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps.booking.entity.Showtime;
import com.ps.booking.service.ShowService;

@RestController
@RequestMapping("/shows")
public class ShowController {
    
    @Autowired
    private ShowService showService;

    @GetMapping
    public ResponseEntity<List<Showtime>> getShows(@RequestParam Long[] theaterIds, @RequestParam LocalDate date) {
        List<Showtime> shows = showService.findShows(theaterIds, date);
        return ResponseEntity.ok(shows);
    }    

    
    @GetMapping("/movies")
    public ResponseEntity<List<Showtime>> getShowsWithMovie(@RequestParam Long[] theaterIds, @RequestParam Long movieId, @RequestParam LocalDate date) {
        List<Showtime> shows = showService.findShowsWithMovie(theaterIds, date, movieId);
        return ResponseEntity.ok(shows);
    }    
}
