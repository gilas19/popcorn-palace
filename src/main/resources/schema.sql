-- Drop tables if they exist to avoid conflicts
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS showtimes;
DROP TABLE IF EXISTS movies;

-- Create movies table
CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    genre VARCHAR(100) NOT NULL,
    duration INTEGER NOT NULL,
    rating DOUBLE PRECISION NOT NULL,
    release_year INTEGER NOT NULL
);

-- Create showtimes table
CREATE TABLE IF NOT EXISTS showtimes (
    id SERIAL PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    theater VARCHAR(100) NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- Create bookings table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id UUID PRIMARY KEY,
    showtime_id BIGINT NOT NULL,
    seat_number INTEGER NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    FOREIGN KEY (showtime_id) REFERENCES showtimes(id)
);