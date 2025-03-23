-- Insert sample movies
INSERT INTO movies (title, genre, duration, rating, release_year) VALUES
('The Avengers', 'Action', 143, 8.0, 2012),
('Inception', 'Sci-Fi', 148, 8.8, 2010),
('The Shawshank Redemption', 'Drama', 142, 9.3, 1994),
('The Dark Knight', 'Action', 152, 9.0, 2008),
('Pulp Fiction', 'Crime', 154, 8.9, 1994);

-- Insert sample showtimes
INSERT INTO showtimes (movie_id, theater, start_time, end_time, price) VALUES
(1, 'Theater 1', '2025-04-01T10:00:00Z', '2025-04-01T12:30:00Z', 15.99),
(1, 'Theater 2', '2025-04-01T14:00:00Z', '2025-04-01T16:30:00Z', 15.99),
(2, 'Theater 1', '2025-04-01T13:00:00Z', '2025-04-01T15:30:00Z', 14.99),
(3, 'Theater 3', '2025-04-01T19:00:00Z', '2025-04-01T21:30:00Z', 16.99),
(4, 'Theater 2', '2025-04-01T20:00:00Z', '2025-04-01T22:30:00Z', 17.99),
(5, 'Theater 1', '2025-04-01T22:00:00Z', '2025-04-02T00:30:00Z', 18.99);