package com.ps.booking.dto;

import java.util.List;

import com.ps.booking.entity.Showtime;
import com.ps.booking.entity.Theater;

import lombok.Data;

@Data
public class TheaterWithShowtimesDto {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String email;
    private double latitude;
    private double longitude;
    private List<Showtime> showtimes; 
    private double distance;

    public TheaterWithShowtimesDto(Theater th){
        this.id = th.getId();
        this.name = th.getName();
        this.address = th.getAddress();
        this.city = th.getCity();
        this.state = th.getState();
        this.zip = th.getZip();
        this.phoneNumber = th.getPhoneNumber();
        this.email = th.getEmail();
        this.latitude = th.getLatitude();
        this.longitude = th.getLongitude();
    }
}
