🗓️ ScheduleApp

Java Backend Application built with Spring Boot, Spring Security, Spring Data JPA, Hibernate, PostgreSQL, JDBC, SQL, and Thymeleaf.

A simple schedule management system demonstrating:

Server-side rendering (Thymeleaf)

Session-based authentication (Spring Security)

JPA/Hibernate entity listeners

Optimistic locking for safe concurrent updates

CRUD operations for schedule items, students, subjects anf groups

🛠️ Technologies

Java 17, Spring Boot 3.4

Backend: Spring Security, Spring Data JPA, Hibernate

Concurrency safety: Optimistic Locking (@Version)

Entity Lifecycle: @PrePersist, @PreUpdate, custom listeners

Database: H2 (in-memory demo), JDBC, SQL

Frontend: Thymeleaf templates

Testing: JUnit 5, Mockito, Jacoco

Build tool: Gradle

⚡ Features
🔐 Authentication

Spring Security

Session-based login (not JWT)

Login, registration, protected pages

📅 Schedule Management

Create, read, update, delete schedule items, students, subjects and groups

Validation on all forms

Optimistic locking (prevents conflicts when two users edit same record)

🔍 Persistence Layer

JPA entities with:

@Version field for concurrency control

@EntityListeners for automatic timestamps, auditing, and logging

🖥️ Frontend

Server-side rendered HTML (Thymeleaf)

Form-based workflow

Error messages, validation hints

🧪 Testing

Unit tests with JUnit & Mockito

Integration tests

Code coverage via Jacoco

🚀 Quick Start

1️⃣ Clone the repository
git clone https://github.com/DmitryKustovskiy/schedule.git
cd schedule

2️⃣ Run the application (H2 in-memory)
./gradlew bootRun --args='--spring.profiles.active=h2'


🌐 App URL:
http://localhost:8080/register

🗄️ H2 Console:
http://localhost:8080/h2-console

H2 is fully in-memory → tables auto-create → data resets on every restart.
Perfect for demo/testing. No PostgreSQL config required.

3️⃣ Run tests & generate coverage
./gradlew test
./gradlew jacocoTestReport


📄 Coverage report:
build/reports/jacoco/test/html/index.html

📁 Project Structure (short overview)
src/
 ├─ main/
 │   ├─ java/yourapp/
 │   │    ├─ controllers/      # MVC controllers
 │   │    ├─ services/         # business logic
 │   │    ├─ repositories/     # Spring Data JPA
 │   │    ├─ entities/         # JPA entities + @Version
 │   │    ├─ listeners/        # Entity listeners (@PrePersist, @PreUpdate)
 │   │    └─ security/         # Security config, user details
 │   └─ resources/
 │        ├─ templates/        # Thymeleaf HTML
 │        ├─ application.yaml
 │        └─ static/
 └─ test/                      # unit & integration tests

👤 Author

Dmitriy Kustovskiy
Java Backend Developer

GitHub: https://github.com/DmitryKustovskiy
