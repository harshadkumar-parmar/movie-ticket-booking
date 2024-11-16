package com.ps.booking.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;


@Entity
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private int duration;
    private String[] genre;
    private String[] languages;
    private String posterUrl;
    private String trailerUrl;
    private String releaseDate;
    private String director;
    // private String cast;

    @OneToMany(mappedBy = "movie")
    private List<Showtime> showtimes;
}
