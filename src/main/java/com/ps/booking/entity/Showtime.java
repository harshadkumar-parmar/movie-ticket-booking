package com.ps.booking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime showtime;
    private LocalDateTime endtime;
    private LocalDate showdate;

    private String language;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Screen screen;

    @ManyToOne
    private Theater theater;

}