package com.ps.booking.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int seatCount;

    @ManyToOne
    private Theater theater;

    @OneToMany(mappedBy = "screen")
    private List<Seat> seats;
    
    @OneToMany(mappedBy = "screen")
    private List<Showtime> showtimes;
}