package com.ps.booking.repository;

import org.springframework.data.repository.CrudRepository;

import com.ps.booking.entity.Movie;

public interface MovieRepository extends CrudRepository<Movie, Object> {
    
}
