🗓️ ScheduleApp
Spring Boot + Thymeleaf + Security + JPA + H2/PostgreSQL

A complete training project with DTO/mappers, optimistic locking, event listeners, layered architecture, and full testing coverage.

🚀 Overview

ScheduleApp is a server-side Java application for managing study schedules.
The project demonstrates solid backend engineering using:

Spring Boot 3

Spring Security (session-based authentication)

Spring Data JPA + Hibernate

Thymeleaf (server-side rendering)

H2 (demo) / PostgreSQL (production)

DTO + Mapper layer

Optimistic Locking (@Version)

EntityGraph, JOIN FETCH optimization

Application & Security Event Listeners

Unit + Integration tests

The application includes a full UI (non-REST) for managing:

groups

subjects

students

schedule items

It also provides full validation, error handling, and service-layer business logic.

🛠️ Technologies
Backend

Java 17

Spring Boot 3.4

Spring Security

Spring Data JPA

Hibernate

DTO & Mapper layer

ApplicationListener events

Database

H2 in-memory (development/demo)

PostgreSQL (production)

Frontend

Thymeleaf (HTML templates)

Server-side rendering only

Testing

JUnit 5

Mockito

SpringBootTest Integration Testing

Jacoco coverage reports

Build

Gradle 8

✨ Features
📌 Schedule Management

Create, edit, delete schedule items

Assign groups and subjects

Filter schedules by group name

Get unique schedule dates

View daily schedules

🗂️ Directories

Full CRUD for:

Groups

Subjects

Students

Schedule Items

🔐 Security

Spring Security (form login)

Session-based authentication

Custom login page

Logout

Login/Logout event listeners

🧩 Architecture Concepts

DTO → Mapper → Entity separation

Service-layer business logic

Repository-level optimization

@EntityGraph and JOIN FETCH for N+1 query reduction

Optimistic locking with @Version

Custom exceptions + global error handler

🧪 Testing

Integration tests for:

CRUD operations

Optimistic locking

Filtering

Entity relations

Unit tests for:
Services, controllers

small utility methods

🚀 Quick Start
1️⃣ Clone the project
git clone https://github.com/DmitryKustovskiy/schedule.git
cd schedule

2️⃣ Run with H2 (demo mode)
./gradlew bootRun --args='--spring.profiles.active=h2'

Application:
http://localhost:8080/register

H2 Console:
http://localhost:8080/h2-console

H2 tables are generated automatically.

3️⃣ Run all tests
./gradlew test
./gradlew jacocoTestReport


Coverage report:
build/reports/jacoco/test/html/index.html

👤 Author

Dmitriy Kustovskiy — Java Backend Developer

🔗 GitHub: https://github.com/DmitryKustovskiy

💼 LinkedIn: https://www.linkedin.com/in/dmitry-kustovskiy/
