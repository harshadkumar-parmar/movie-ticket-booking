INSERT INTO theater (id, name, address, city, state, zip, phone_number, email)
VALUES
  (1, 'PVR Cinemas', 'Ahmedabad One Mall, Vastrapur, Ahmedabad - 380015', 'Ahmedabad', 'Gujarat', '380015', '079-40001234', 'pvrcinemas@pvrcinemas.com'),
  (2, 'INOX Leisure Ltd', 'Gujarat Samachar Press House, Ashram Road, Ahmedabad - 380009', 'Ahmedabad', 'Gujarat', '380009', '079-26571234', 'inoxleisure@inoxleisure.com'),
  (3, 'Cinepolis', 'Alpha One Mall, Vastrapur, Ahmedabad - 380015', 'Ahmedabad', 'Gujarat', '380015', '079-40005678', 'cinepolis@cinepolis.com'),
  (4, 'Miraj Cinemas', 'Miraj City Centre, Ring Road, Surat - 395002', 'Surat', 'Gujarat', '395002', '0261-2478123', 'mirajcinemas@mirajcinemas.com'),
  (5, 'Rajhans Cinemas', 'Rajhans Multiplex, Opp. Sardar Patel Stadium, Navsari - 396445', 'Navsari', 'Gujarat', '396445', '02637-244444', 'rajhanscinemas@rajhanscinemas.com');

  INSERT INTO screen (id, theater_id, screen_number, seat_capacity)
  VALUES
    (1, 1, 1, 200),
    (2, 1, 2, 250),
    (3, 1, 3, 300),
    (4, 2, 1, 400),
    (5, 2, 2, 350),
    (6, 3, 1, 500),
    (7, 3, 2, 450),
    (8, 4, 1, 300),
    (9, 4, 2, 250),
    (10, 5, 1, 200);

INSERT INTO movie (id, title, genre, runtime, rating)
VALUES
  (1, 'Avengers: Endgame', 'Action', 181, 'PG-13'),
  (2, 'The Lion King', 'Animation', 118, 'PG'),
  (3, 'Joker', 'Drama', 122, 'R'),
  (4, 'Toy Story 4', 'Animation', 104, 'G'),
  (5, 'Spider-Man: Far From Home', 'Action', 129, 'PG-13'),
  (6, 'Captain Marvel', 'Action', 124, 'PG-13'),
  (7, 'Aladdin', 'Fantasy', 128, 'PG'),
  (8, 'The Secret Life of Pets 2', 'Animation', 86, 'PG'),
  (9, 'Men in Black: International', 'Science Fiction', 115, 'PG-13'),
  (10, 'John Wick: Chapter 3 – Parabellum', 'Action', 131, 'R');

  INSERT INTO showtime (id, movie_id, screen_id, showtime_date, showtime_time)
  VALUES
    (1, 1, 1, '2024-10-09', '10:00 AM'),
    (2, 1, 1, '2024-10-09', '1:00 PM'),
    (3, 1, 1, '2024-10-09', '4:00 PM'),
    (4, 2, 2, '2024-10-09', '10:30 AM'),
    (5, 2, 2, '2024-10-09', '1:30 PM'),
    (6, 3, 3, '2024-10-09', '11:00 AM'),
    (7, 3, 3, '2024-10-09', '2:00 PM'),
    (8, 4, 4, '2024-10-09', '10:00 AM'),
    (9, 4, 4, '2024-10-09', '12:30 PM'),
    (10, 5, 5, '2024-10-09', '11:30 AM');