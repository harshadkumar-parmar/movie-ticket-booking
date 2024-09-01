# Transactions Routine API

This project is a simple API for managing customer transactions. It includes endpoints for creating accounts and transactions, and retrieving account information. The project is built using Java and Spring Boot.

## Table of Contents
- Getting Started
- Endpoints
- Running Tests
- Swagger API Documentation
- Security Configuration
- Database Structure

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven
- Docker (optional, for bonus points)

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/transactions-routine.git
    cd transactions-routine
    ```

2. Build the project:
    ```bash
    mvn clean install
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

### Docker
To run the application using Docker:
1. Build the Docker image:
    ```bash
    docker build -t transactions-routine .
    ```

2. Run the Docker container:
    ```bash
    docker run -p 8080:8080 transactions-routine
    ```

## Endpoints

### Create an Account
- **URL:** `/accounts`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
      "document_number": "12345678900"
    }
    ```
- **Response Body:**
    ```json
    {
      "account_id": 1,
      "document_number": "12345678900"
    }
    ```

### Retrieve Account Information
- **URL:** `/accounts/{accountId}`
- **Method:** `GET`
- **Response Body:**
    ```json
    {
      "account_id": 1,
      "document_number": "12345678900"
    }
    ```

### Create a Transaction
- **URL:** `/transactions`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
      "account_id": 1,
      "operation_type_id": 4,
      "amount": 123.45
    }
    ```

## Unit Tests

Unit tests are essential to ensure that your application behaves as expected. In this project, unit tests are written using JUnit and Spring Boot's testing framework. These tests cover various aspects of the application, including service logic and controller endpoints.

### Running Unit Tests
To run the unit tests, use the following command:
```bash
mvn test
```

## Swagger API Documentation

Swagger is used to generate interactive API documentation for this project. It allows you to explore and test the API endpoints directly from the browser.

### Accessing Swagger UI

Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html

```


/swagger-ui.html

## Security Configuration
The application includes security configurations for authentication and authorization. Ensure you have the necessary credentials to access the endpoints.

## Contact
If you have any questions, feel free to reach out.

## Database Structure

```mermaid
erDiagram
    ACCOUNT {
        Long account_id PK
        String document_number
        String name
        String email
        String password
    }
    OPERATION_TYPE {
        short operation_type_id PK
        String description
        boolean is_negative
    }
    TRANSACTION {
        Long transaction_id PK
        Long account_id FK
        short operation_type_id FK
        BigDecimal amount
        LocalDateTime event_date
    }

    ACCOUNT ||--o{ TRANSACTION : has
    OPERATION_TYPE ||--o{ TRANSACTION : has

```