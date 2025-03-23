# Popcorn Palace - Setup and Running Instructions

## Environment Setup

### Prerequisites

1. Java 21 (JDK)
2. Docker Desktop
3. Git

### Clone the Repository

```bash
git clone https://github.com/gilas19/popcorn-palace.git
cd popcorn-palace
```

## Database Setup

Start the PostgreSQL database using Docker:

```bash
docker-compose up -d
```

This will start a PostgreSQL instance with the following configuration:

- Host: localhost
- Port: 5432
- Database: popcorn
- Username: postgres
- Password: postgres

The schema and initial data will be loaded automatically using the SQL files provided in the project.

## Building the Application

```bash
./mvnw clean install
```

## Running the Application

```bash
./mvnw spring-boot:run
```

Once the application is running, you can access the API endpoints at:

- Base URL: <http://localhost:8080>

### Example API Calls

#### Get all movies

```http
GET http://localhost:8080/movies/all
```

#### Add a movie

```http
POST http://localhost:8080/movies
Content-Type: application/json

{
  "title": "Sample Movie Title",
  "genre": "Action",
  "duration": 120,
  "rating": 8.7,
  "releaseYear": 2025
}
```

#### Get showtime by ID

```http
GET http://localhost:8080/showtimes/1
```

#### Book a ticket

```http
POST http://localhost:8080/bookings
Content-Type: application/json

{
  "showtimeId": 1,
  "seatNumber": 15,
  "userId": "84438967-f68f-4fa0-b620-0f08217e76af"
}
```

## Testing the APIs

```bash
./mvnw test
```

For easier API testing, you can use the Postman collection file included in the project (`popcorn-palace.postman_collection.json`).