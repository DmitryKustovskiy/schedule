Schedule Application

A Spring Boot web application for managing schedules. Built with Java, Thymeleaf, Spring Data JPA, Spring Security, and H2 database for quick setup.

Features

CRUD operations for schedule items

User authentication and authorization with Spring Security

Server-side rendered UI with Thymeleaf

REST API endpoints for schedule management

Unit and integration testing with JUnit and Mockito

Easy setup with in-memory H2 database

Technologies

Java 17

Spring Boot 3

Spring Core / Spring Security / Spring Data JPA / Hibernate

Thymeleaf

H2 database (in-memory)

Gradle

JUnit, Mockito

Lombok

Setup & Run

1️⃣ Clone the repository
git clone https://github.com/DmitryKustovskiy/schedule.git
cd schedule

2️⃣ Run the application using H2 database
./gradlew bootRun --args='--spring.profiles.active=h2'


The app will use an in-memory H2 database. No additional database setup is required.

3️⃣ Open in browser

Application UI: http://localhost:8080/register

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:schedule

User: sa

Password: (leave empty)

4️⃣ Running Tests
./gradlew test


HTML code coverage report: build/reports/jacoco/test/html/index.html

Project Structure

controller/ – REST & Thymeleaf controllers

service/ – Business logic, CRUD operations

repository/ – JPA repositories

model/ – Entities (ScheduleItem, Group, Subject)

security/ – JWT and Spring Security configuration

dto/ – Data transfer objects

mapper/ – Mapping between entities and DTOs

Notes

Uses H2 in-memory database for demo purposes.

Switching to PostgreSQL requires updating application.yaml with database credentials.

Author

Dmitriy Kustovskiy

20+ years of experience as a radio host (Pilot FM, Unistar)

Strong communication & leadership skills

GitHub: https://github.com/DmitryKustovskiy
