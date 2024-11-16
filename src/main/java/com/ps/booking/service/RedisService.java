package com.ps.booking.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;




@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * Holds the specified seats for a show for a given time-to-live duration.
     * 
     * @param showId the identifier of the show
     * @param seatNumber the array of seat numbers to hold
     * @param ttlInSeconds the time-to-live in seconds for which the seats should be held
     */
    public void holdSeats(String showId, Long[] seatNumber, long ttlInSeconds) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        for (Long seat : seatNumber) {
            String key = "show:" + showId + ":seat:" + seat;
            ops.set(key, "hold", ttlInSeconds, TimeUnit.SECONDS);
        }
    }

    /**
     * Return true if the seats are already booked for the given showId
     * @param showId the show identifier
     * @param seatNumber the seat numbers to check
     * @return true if the seats are already booked, false otherwise
     */
    public boolean areSeatsHold(String showId, Long[] seatNumber) {
        for (Long seat : seatNumber) {
            String key = "show:" + showId + ":seat:" + seat;
            if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                return false;  // Seat not reserved
            }
        }
        return true;
    }
}
