package com.ps.booking.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.ps.booking.entity.Theater;

public interface TheaterRepository extends CrudRepository<Theater, Long> {

    List<Theater> findByCity(String city);
}
