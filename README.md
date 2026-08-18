# spring-boot-google-recaptcha-sample

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-google-recaptcha-sample/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-google-recaptcha-sample/actions/workflows/maven.yml)

A sample Spring Boot application that protects a login form with **Google reCAPTCHA v3**, falling back to an OTP
challenge whenever reCAPTCHA returns a low score. Authentication is backed by Spring Security with a custom
`UsernamePasswordAuthenticationFilter` that verifies the reCAPTCHA token before delegating to the normal
authentication flow.

## Tech Stack

- Java 25
- Spring Boot 4.1.0
- Spring Security 7
- Spring Data JPA / Hibernate
- MySQL 8
- Thymeleaf
- Lombok
- Maven (wrapper included)

## How it works

1. `GET /login` renders a login form protected by Google reCAPTCHA v3 (`login.html`).
2. On submit, the reCAPTCHA widget obtains a token and posts the form to `POST /processLogin`.
3. `CustomLoginFilter` intercepts the request, sends the token to Google's `siteverify` endpoint via
   `ReCaptchaV3Handler`, and reads back a human/bot score.
    - Score `>= 0.5`: the request proceeds to normal username/password authentication.
    - Score `< 0.5`: the request is forwarded to an OTP challenge (`otp_login.html`) instead.
4. On successful authentication, the user is redirected to `/success`.

## Prerequisites

- JDK 25
- MySQL 8+ running locally (or via Docker) on `localhost:3306`

## Configuration

Datasource and JPA settings live in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/recaptcha?createDatabaseIfNotExist=true&...
spring.datasource.username=root
spring.datasource.password=root
```

Adjust the username/password to match your local MySQL instance. The database (`recaptcha`) is created
automatically on first connection.

The reCAPTCHA v3 site key (in `login.html`) and secret key (in `ReCaptchaV3Handler`) are Google's publicly
documented **test keys**, which always pass verification. Replace them with your own keys from the
[reCAPTCHA admin console](https://www.google.com/recaptcha/admin) for real deployments.

### Starting MySQL with Docker

```bash
docker run -d --name recaptcha-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=recaptcha \
  -p 3306:3306 \
  mysql:8.0
```

## Running the app

```bash
./mvnw spring-boot:run
```

Or build a jar and run it directly:

```bash
./mvnw clean package
java -jar target/recaptchaV3-0.0.1-SNAPSHOT.jar
```

The app starts on [http://localhost:8080/login](http://localhost:8080/login).

On first startup, two demo users are seeded (password `1234` for both):

| Username | Role       |
|----------|------------|
| naruto   | ROLE_USER  |
| gojo     | ROLE_ADMIN |

## Running tests

```bash
./mvnw test
```

Tests require a reachable MySQL instance matching the datasource configuration above (CI spins one up
automatically — see below).

## Continuous Integration

GitHub Actions (`.github/workflows/maven.yml`) builds the project with JDK 25 on every push/PR to `main`,
using a MySQL 8 service container so `mvn package` (which runs the test suite) can connect to a real database.
