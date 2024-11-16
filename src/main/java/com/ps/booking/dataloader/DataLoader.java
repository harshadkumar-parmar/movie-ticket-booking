package com.ps.booking.dataloader;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ps.booking.entity.Customer;
import com.ps.booking.entity.Movie;
import com.ps.booking.entity.Screen;
import com.ps.booking.entity.Seat;
import com.ps.booking.entity.SeatType;
import com.ps.booking.entity.Showtime;
import com.ps.booking.entity.Theater;
import com.ps.booking.repository.CustomerRepository;
import com.ps.booking.repository.MovieRepository;
import com.ps.booking.repository.ScreenRepository;
import com.ps.booking.repository.SeatRepository;
import com.ps.booking.repository.ShowtimeRepository;
import com.ps.booking.repository.TheaterRepository;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public void run(String... args) {
        long customerCount = customerRepository.count();
        if (customerCount > 0) {
            return;
        }
        Customer customer = new Customer();
        customer.setName("John2");
        customer.setMobileNumber("123-456-7890");
        customer.setEmail("z2m9u@example.com");
        customer.setPassword("password");

        Customer customer2 = new Customer();
        customer2.setName("John");
        customer2.setMobileNumber("123-456-7891");
        customer2.setEmail("john.doe@example.com");
        customer2.setPassword("password");

        // Save Customers
        customerRepository.saveAll(Arrays.asList(customer, customer2));

        Theater theater = new Theater();
        theater.setName("PVR Cinemas");
        theater.setAddress("Ahmedabad One Mall, Vastrapur, Ahmedabad - 380015");
        theater.setCity("Ahmedabad");
        theater.setState("Gujarat");
        theater.setZip("380015");
        theater.setPhoneNumber("079-40001234");
        theater.setEmail("pvrcinemas@pvrcinemas.com");
        theater.setLatitude(23.02);
        theater.setLongitude(72.51);

        Theater theater2 = new Theater();
        theater2.setName("Cinepolis");
        theater2.setAddress("Manishvi, Vastrapur, Ahmedabad - 380015");
        theater2.setCity("Ahmedabad");
        theater2.setState("Gujarat");
        theater2.setZip("380015");
        theater2.setPhoneNumber("079-40001234");
        theater2.setEmail("cinepolis@cinepolis.com");
        theater2.setLatitude(23.13);
        theater2.setLongitude(72.31);

        Theater theater3 = new Theater();
        theater3.setName("PVR Arved Transcube Plaza");
        theater3.setAddress("Arved, Vastrapur, Ahmedabad - 380015");
        theater3.setCity("Ahmedabad");
        theater3.setState("Gujarat");
        theater3.setZip("380015");
        theater3.setPhoneNumber("079-40001234");
        theater3.setEmail("pvrcinemas@pvrcinemas.com");
        theater3.setLatitude(23.49);
        theater3.setLongitude(72.11);

        Theater theater4 = new Theater();
        theater4.setName("Cinepolis");
        theater4.setAddress("Minabazar Vadodara - 390001");
        theater4.setCity("Vadodara");
        theater4.setState("Gujarat");
        theater4.setZip("390001");
        theater4.setPhoneNumber("079-40001234");
        theater4.setEmail("cinepolis@cinepolis.com");
        theater4.setLatitude(22.31);
        theater4.setLongitude(73.18);

        List<Theater> theaters = Arrays.asList(theater, theater2, theater3, theater4);

        Movie movie = new Movie();
        movie.setTitle("Avatar: The Way of Water");
        movie.setDescription("Jake Sully and Neytiri become parents to a new life on the exo-planet of Pandora.");
        movie.setDuration(192);
        movie.setGenre(new String[] { "Action", "Adventure", "Fantasy" });
        movie.setLanguages(new String[] { "English", "Spanish" });
        movie.setDirector("James Cameron");
        movie.setPosterUrl(
                "https://upload.wikimedia.org/wikipedia/en/5/54/Avatar_The_Way_of_Water_poster.jpg");
        movie.setTrailerUrl("https://www.youtube.com/watch?v=a8Gx8wiNbs8");
        movie.setReleaseDate("2022-12-14");
        movie = movieRepository.save(movie);

        Movie movie2 = new Movie();
        movie2.setTitle("Avengers: Endgame");
        movie2.setDescription(
                "After Thanos, an intergalactic warlord, disintegrates half of the universe, the Avengers must assemble once more to undo what he did.");
        movie2.setDuration(181);
        movie2.setGenre(new String[] { "Action", "Adventure", "Drama" });
        movie2.setLanguages(new String[] { "English", "Spanish" });
        movie2.setDirector("Anthony Russo, Joe Russo");
        // movie2.setCast("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris
        // Hemsworth");
        movie2.setPosterUrl(
                "https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg");
        movie2.setTrailerUrl("https://www.youtube.com/watch?v=TcMBFSGVi1c");
        movie2.setReleaseDate("2019-04-24");
        movie2 = movieRepository.save(movie2);

        for (Theater th : theaters) {

            Screen screen = new Screen();
            screen.setSeatCount(30);
            screen.setName("Screen 1");
            screen.setSeats(new ArrayList<>());

            for (int i = 1; i <= 10; i++) {
                Seat seat = new Seat();
                seat.setRow(i / 5);
                seat.setSeatType(SeatType.RECLINER);
                seat.setScreen(screen);
                seat.setSeatNumber("" + i);
                screen.getSeats().add(seat);
            }

            for (int i = 0; i < 20; i++) {
                Seat seat = new Seat();
                int row = i / 5;
                seat.setRow(row + 2);
                seat.setSeatNumber("" + i + 11);
                seat.setSeatType(SeatType.REGULAR);
                seat.setScreen(screen);
                screen.getSeats().add(seat);
            }
            seatRepository.saveAll(screen.getSeats());
            screenRepository.save(screen);
            theater.setScreens(Arrays.asList(screen));
            theaterRepository.save(th);
        }

            Showtime showtime = new Showtime();
            showtime.setShowtime(LocalDateTime.now());
            showtime.setEndtime(LocalDateTime.now().plusMinutes(movie.getDuration()));
            showtime.setLanguage("English");
            showtime.setMovie(movie);
            showtime.setScreen(theater.getScreens().get(0)); 
            showtime.setTheater(theater);
            
            Showtime showtime2 = new Showtime();
            showtime2.setShowtime(LocalDateTime.now());
            showtime2.setEndtime(LocalDateTime.now().plusMinutes(movie2.getDuration()));
            showtime2.setLanguage("English");
            showtime2.setMovie(movie2);
            showtime2.setScreen(theater.getScreens().get(0)); 
            showtime2.setTheater(theater);

            Showtime showtime3 = new Showtime();
            showtime3.setShowtime(LocalDateTime.now());
            showtime3.setEndtime(LocalDateTime.now().plusHours(3).plusHours(movie.getDuration()));
            showtime3.setLanguage("English");
            showtime3.setMovie(movie);
            showtime3.setScreen(theater.getScreens().get(0)); 
            showtime3.setTheater(theater);

            Showtime showtime4 = new Showtime();
            showtime4.setShowtime(LocalDateTime.now());
            showtime4.setEndtime(LocalDateTime.now().plusHours(3).plusMinutes(movie2.getDuration()));
            showtime4.setLanguage("English");
            showtime4.setMovie(movie2);
            showtime4.setScreen(theater.getScreens().get(0)); 
            showtime4.setTheater(theater2);

            Showtime showtime5 = new Showtime();
            showtime5.setShowtime(LocalDateTime.now());
            showtime5.setEndtime(LocalDateTime.now().plusHours(3).plusMinutes(movie2.getDuration()));
            showtime5.setLanguage("English");
            showtime5.setMovie(movie2);
            showtime5.setScreen(theater.getScreens().get(0)); 
            showtime5.setTheater(theater3);

            Showtime showtime6 = new Showtime();
            showtime6.setShowtime(LocalDateTime.now());
            showtime6.setEndtime(LocalDateTime.now().plusHours(3).plusMinutes(movie2.getDuration()));
            showtime6.setLanguage("English");
            showtime6.setMovie(movie2);
            showtime6.setScreen(theater.getScreens().get(0)); 
            showtime6.setTheater(theater4);

            Showtime showtime7 = new Showtime();
            showtime7.setShowtime(LocalDateTime.now());
            showtime7.setEndtime(LocalDateTime.now().plusHours(6).plusMinutes(movie2.getDuration()));
            showtime7.setLanguage("English");
            showtime7.setMovie(movie2);
            showtime7.setScreen(theater.getScreens().get(0)); 
            showtime7.setTheater(theater3);

            Showtime showtime8 = new Showtime();
            showtime8.setShowtime(LocalDateTime.now());
            showtime8.setEndtime(LocalDateTime.now().plusHours(6).plusMinutes(movie2.getDuration()));
            showtime8.setLanguage("English");
            showtime8.setMovie(movie2);
            showtime8.setScreen(theater.getScreens().get(0)); 
            showtime8.setTheater(theater4);

            showtimeRepository.saveAll(Arrays.asList(showtime, showtime2, showtime3, showtime4, showtime5, showtime6, showtime7, showtime8));

    }
}
