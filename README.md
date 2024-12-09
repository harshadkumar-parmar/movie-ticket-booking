# Movie Ticket Booking System

## Overview

This project is a movie ticket booking system built with Spring Boot, JPA, and Redis for synchronization and locking. It allows users to book multiple seats for a movie showtime, ensuring concurrency and consistency in a distributed environment.

## Table of Contents
- Features
- Technologies Used
- Endpoints
- Running Tests
- Swagger API Documentation
- Security Configuration
- Database Structure
- Depedency Report

## Features

- Book multiple seats for a movie showtime
- Concurrent seat reservation with Redis distributed locks
- Release expired seat reservations
- Confirm seat reservations
- Find theaters by movie ID and show date

## Technologies Used

- Spring Boot
- Spring Data JPA
- Redisson (Redis-based distributed locks)
- Postgresql (for development and testing)
- Maven (for dependency management)

## Prerequisites

- JDK 17
- Maven
- Redis server (running on localhost:6379)
- Postgresql
- Docker


### Run application with Docker compose

1. <b>Clone the repository </b>:
    ```bash
    git clone https://github.com/harsh2792/transaction-service.git
    cd transactions-service
    ```
 
2. <b>Create build using maven </b>:

```bash
mvn clean package -DskipTests
```

3. <b>Build the Docker Images</b>: Build the Docker images using the following command:
```bash
docker-compose build
```

4. <b>Verify the Services</b>: Verify that the services are running correctly by checking the logs:

```bash
docker-compose logs
```

5. <b>Stop the Services</b>: Stop the services using the following command:

```bash
docker-compose down
```

## Endpoints

TODO: Endpoint details

## Unit Tests

Unit tests are essential to ensure that your application behaves as expected. In this project, unit tests are written using JUnit and Spring Boot's testing framework. These tests cover various aspects of the application, including service logic and controller endpoints.

### Running Unit Tests
To run the unit tests, use the following command:
```bash
mvn clean test
```

### Generate test report
To run the unit tests, use the following command:
```bash
mvn jococo:report
```

or can use run-test.sh file to test and generate reports

```bash
sh ./run-test.sh
```


## Swagger API Documentation

Swagger is used to generate interactive API documentation for this project. It allows you to explore and test the API endpoints directly from the browser.

### Accessing Swagger UI

Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html

```

## Security Configuration
The application includes security configurations for authentication and authorization. Ensure you have the necessary credentials to access the endpoints.

## Database Structure

```mermaid
    erDiagram

    CUSTOMER {
        bigint id PK
        varchar email
        varchar mobile_number
        varchar name
        varchar password
    }
    
    MOVIE {
        bigint id PK
        varchar description
        varchar director
        int duration
        varchar[] genre
        varchar[] languages
        varchar poster_url
        varchar release_date
        varchar title
        varchar trailer_url
    }
    
    THEATER {
        bigint id PK
        varchar address
        varchar city
        varchar email
        double latitude
        double longitude
        varchar name
        varchar phone_number
        varchar state
        varchar zip
    }
    
    SCREEN {
        bigint id PK
        varchar name
        int seat_count
        bigint theater_id FK
    }
    
    SEAT {
        bigint id PK
        int row
        varchar seat_number
        varchar seat_type
        bigint screen_id FK
    }
    
    SHOWTIME {
        bigint id PK
        timestamp endtime
        varchar language
        timestamp showtime
        timestamp showdate
        bigint movie_id FK
        bigint screen_id FK
        bigint theater_id FK
    }
    
    BOOKING {
        bigint id PK
        timestamp booking_date
        varchar booking_status
        double discount_amount
        varchar payment_status
        double tax_amount
        double total_amount
        bigint customer_id FK
        bigint showtime_id FK
    }
    
    SEAT_RESERVATION {
        bigint id PK
        timestamp reservation_date
        boolean reserved
        bigint booking_id FK
        bigint seat_id FK
    }
    
    TRANSACTION {
        bigint transaction_id PK
        numeric amount
        timestamp event_date
        bigint account_id FK
    }

    CUSTOMER ||--o{ BOOKING : "has many"
    CUSTOMER ||--o{ TRANSACTION : "has many"
    BOOKING ||--o{ SEAT_RESERVATION : "contains"
    BOOKING }o--|| SHOWTIME : "for"
    SEAT_RESERVATION }o--|| SEAT : "assigned"
    SCREEN ||--o{ SEAT : "contains"
    THEATER ||--o{ SCREEN : "has many"
    SHOWTIME }o--|| MOVIE : "for"
    SHOWTIME }o--|| SCREEN : "shown on"
    SHOWTIME }o--|| THEATER : "in"

```

## Depedency Report

Use the following Maven command to generate the report:
```bash
mvn dependency-check:check

```
the OWASP Dependency-Check report


## Diagrams

### Flowcharts

```mermaid


flowchart TD
    Start([Start])
    SelectShowSeat([User Selects Show and Seat])
    CheckAvailability{"Check Seat Availability in Redis"}
    IsSeatAvailable{"Is Seat Available?"}
    ReserveSeat([Reserve Seat Temporarily in Redis - TTL])
    ErrorMessage([Show Error Message])
    ConfirmPrompt([Display Reservation Confirmation Prompt to User])
    ConfirmBooking{"Does User Confirm Booking in Time?"}
    FinalizeBooking([Confirm Booking and Mark Seat as Booked in Redis])
    ReleaseAfterTTL([Release Seat After TTL Expiration])
    End([End])

    Start --> SelectShowSeat
    SelectShowSeat --> CheckAvailability
    CheckAvailability --> IsSeatAvailable
    IsSeatAvailable -- Yes --> ReserveSeat
    IsSeatAvailable -- No --> ErrorMessage
    ReserveSeat --> ConfirmPrompt
    ConfirmPrompt --> ConfirmBooking
    ConfirmBooking -- Yes --> FinalizeBooking
    ConfirmBooking -- No --> ReleaseAfterTTL
    FinalizeBooking --> End
    ReleaseAfterTTL --> End

```

### System Architecture

```mermaid

graph TD;
    Client-->LoadBalancer;
    LoadBalancer-->EKS;
    EKS-->BookingService;
    EKS-->SearchService;
    EKS-->RecommendationEngine;
    EKS-->ElasticSearch;
    EKS-->Redis;
    EKS-->ObjectStore;
    EKS-->PaymentService;
    EKS-->TransactionService;
    BookingService-->Redis;
    PaymentService-->Redis;
    TransactionService-->Redis;
    BookingService-->RDSMaster;
    PaymentService-->RDSMaster;
    TransactionService-->RDSMaster;
    RDSMaster-->RDSReplica;
    SearchService-->ElasticSearch;
    RecommendationEngine-->MLModel;
    Monitoring-->EKS;
    Tracing-->EKS;
    
    subgraph EKS
        BookingService
        PaymentService
        TransactionService
        SearchService
        RecommendationEngine
        ElasticSearch
        Redis
        Monitoring
        Tracing
    end
    
    subgraph RDS
        RDSMaster
        RDSReplica
    end
    
    subgraph ElasticSearch
        MovieIndex
        TheaterIndex
    end
    
    subgraph MLModel
        RecommendationModel
    end
    
    subgraph Monitoring
        Prometheus
        Grafana
    end
    
    subgraph Tracing
        Jaeger
        Zipkin
    end
    
    subgraph Redis
        Cache
        RedisLock
    end
    
    subgraph ObjectStore
        StaticFiles
        Videos
    end


```

## Contact
If you have any questions, feel free to reach out.

