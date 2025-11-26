🗓️ ScheduleApp

Java Backend Project with Spring Boot, Spring Security, Spring Data JPA, Hibernate, JDBC, SQL, and Thymeleaf.

A simple schedule management application demonstrating:

REST APIs

Server-side rendered pages with sessions

JWT authentication

🛠️ Technologies

Java 17, Spring Boot 3.4

Backend: Spring Security, Spring Data JPA, Hibernate

Database: JDBC, SQL, H2 (in-memory demo)

Frontend: Thymeleaf (server-side rendering)

Testing: JUnit & Mockito, Jacoco coverage

Build & Run: Gradle

⚡ Features

View, create, update, delete schedule items

REST API endpoints

Thymeleaf-based UI with session support

JWT authentication for secure API

Unit & integration tests

🚀 Quick Start

Setup & Run

1️⃣ Clone the repository

git clone https://github.com/DmitryKustovskiy/schedule.git
cd schedule

2️⃣ Run the app with H2 in-memory database

./gradlew bootRun --args='--spring.profiles.active=h2'

🌐 Application URL: http://localhost:8080/register

🗄️ H2 console: http://localhost:8080/h2-console

💡 Tip: H2 is in-memory, tables are auto-created, and data will reset on app restart. No PostgreSQL setup needed.

3️⃣ Run tests & generate coverage report

./gradlew test
./gradlew jacocoTestReport

📄 Test report: build/reports/jacoco/test/html/index.html

🔗 Projects

Thymeleaf & sessions demo: GitHub Repo

REST API demo: GitHub Repo

👤 About Me

Dmitriy Kustovskiy – Java Backend Developer with hands-on experience in:

Java Core, Spring Boot, Spring Security, Spring Data JPA, Hibernate, JDBC, SQL, Thymeleaf

Building REST APIs and server-side web apps with JWT & session authentication

Unit & integration testing using JUnit & Mockito

Notes

Uses H2 in-memory database for demo purposes.

Switching to PostgreSQL requires updating application.yaml with database credentials.

Author

Dmitriy Kustovskiy

GitHub: https://github.com/DmitryKustovskiy
>>>>>>> fbf50910b1d9a47c3c1e26df2f94160ba1fb5492
