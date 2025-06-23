# payment-service-assignment
This project is Spring Boot based application for managing account and performing payment transfers.

## Features

- **Create Bank Accounts**: Add new bank account with initial balance
- **Retrieve Bank Account**: Get account details for given account number
- **Payment Transfer**: Perform idempotent payment transfer between accounts.
- **Exception Handling**: Handle errors such as "Account not found", "Insufficient balance" and "Duplicate transactions"
- **Logging**:Comprehensive logging for monitoring.
- **OpenAI Documentation**: Swagger UI for API documentation.

## Prerequisites

- **Java**: Install Java 17 or higher
- **Maven**: Maven for dependency management
- **PostgreSQL**: Install PostgreSQL 14 or higher

## Setup Instruction

### 1. Clone the repository

git clone https://github.com/riteshthakkar/payment-service-assignment.git
cd payment-service-assignment

### 2. Configure PostgreSQL Database

#### 1. Install PostgreSQL
- On Ubuntu 
  - sudo apt update
  - sudo apt install postgresql postgresql -contrib
- On Windows
  - Download and install PostgreSQL from https://www.postgresql.org/download/windows/

#### 2. Create a database
    CREATE DATABASE paymentdb

#### 3. Create user and grant privileges
    CREATE USER postgres WITH password 'root'
    GRANT ALL PREVILEGES ON DATABASE paymentdb to postgres

#### 4. Start DB services
-Create data folder in system (for e.g. on windows C:\Data) 

    initdb -U postgres -A password -E utf8 -W -D C:\data
-Key in password on prompt
-Start service

    pg_ctl -D C:\data -l logfile start


### 3. Configure application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/paymentdb
spring.datasource.username =postgres
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

### 4. Build and Run the application


#### 1. Build the application
  
    mvn clean install

#### 2. Run the application (Make sure DB services are running)
    mvn spring-boot:run

## API Endpoints

- Refer API documentation at  
    http://<server host>:8080/swagger-ui.html
    e.g. http://localhost:8080/swagger-ui.html

## Notes
- All API endpoints starts with /api/v1
- POST /api/v1/transaction - 
  - This endpoints require **Idempotency-Key** to be put in request header.
- No need to create table definition in Postgres database. Table will be created automatically once application starts.