# Spring Security JWT OAuth2 AOP Authentication System

## Overview

This project is a secure authentication and authorization system built using Spring Boot and Spring Security. It combines traditional JWT-based authentication with OAuth2 login integration (Google and GitHub) while maintaining a unified security flow for all users.

The system supports:

* Local authentication using JWT
* OAuth2 login using Google and GitHub
* JWT token generation for OAuth2 users as well
* Stateless authentication architecture
* Role-based access control
* AOP-based centralized logging
* Secure API request handling
* Environment variable based secret management

The main goal of this project is to demonstrate how enterprise-level authentication systems can unify multiple authentication providers under a single JWT-based security mechanism.

---

# Features

## JWT Authentication

* User registration and login
* Secure JWT token generation
* Token validation using custom JWT filters
* Stateless session management
* Secure API access using Bearer tokens
* Configurable token expiration

## OAuth2 Authentication

Supports OAuth2 login with:

* Google
* GitHub

Unlike many basic OAuth2 implementations, this project additionally generates JWT tokens for OAuth2 authenticated users.

This means:

* Local users and OAuth2 users follow the same authorization flow
* All authenticated requests are validated through JWT
* The backend maintains a unified security architecture
* APIs remain stateless regardless of authentication provider

## Spring Security

* Custom security configuration
* Endpoint protection
* Authentication and authorization filters
* Stateless security implementation
* Role-based access management

## AOP Logging

Aspect-Oriented Programming (AOP) is used for centralized activity logging.

Implemented features include:

* Request logging
* Method execution tracking
* Authentication activity monitoring
* Cleaner separation of concerns
* Reduced duplicate logging code

This demonstrates how cross-cutting concerns can be handled cleanly in enterprise applications.

---

# Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT (JSON Web Token)
* OAuth2 Client
* Spring AOP
* Maven
* MySQL
* REST APIs

---

# Authentication Flow

## Local Authentication Flow

1. User registers or logs in using email and password
2. Credentials are authenticated using Spring Security
3. JWT token is generated
4. Client sends JWT token in Authorization header
5. JWT filter validates token for every protected request

---

## OAuth2 Authentication Flow

1. User logs in using Google or GitHub
2. OAuth2 provider authenticates the user
3. User information is fetched from provider
4. Application creates or updates user internally
5. Application generates its own JWT token
6. Future requests are validated using JWT

This hybrid approach ensures consistent authentication handling for all users.

---

# AOP Logging Flow

The project uses Spring AOP to intercept important application events.

Logging includes:

* API request details
* Authentication activity
* Method execution tracking
* Exception tracing

Benefits:

* Cleaner controller/service code
* Better debugging and monitoring
* Centralized logging strategy
* Improved maintainability

---

# Project Structure

```text
src/main/java
│
├── config
├── controller
├── dto
├── entity
├── exception
├── filter
├── repository
├── security
├── service
├── aspect
└── util
```

---

# Setup Instructions

## Prerequisites

Before running the project, make sure you have:

* Java 17+ installed
* Maven installed
* MySQL installed and running
* Google OAuth credentials
* GitHub OAuth credentials

---

# Database Setup

Create a MySQL database:

```sql
CREATE DATABASE your_database_name;
```

---

# Application Properties Configuration

This project uses environment variable placeholders for security purposes.

Update the following values in:

```text
src/main/resources/application.properties
```

Current configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/${DB-NAME}?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=${DB_USER_NAME}
spring.datasource.password=${DB_USER_PASSWORD}

spring.security.oauth2.client.registration.google.client-id=${GOOGLE_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_SECRET}

spring.security.oauth2.client.registration.github.client-id=${GITHUB_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_SECRET}

jwt.secret=${JWT_SECRET}
```

---

# Required Environment Variables

Users need to provide the following values:

| Variable         | Description                     |
| ---------------- | ------------------------------- |
| DB-NAME          | MySQL database name             |
| DB_USER_NAME     | Database username               |
| DB_USER_PASSWORD | Database password               |
| GOOGLE_ID        | Google OAuth Client ID          |
| GOOGLE_SECRET    | Google OAuth Client Secret      |
| GITHUB_ID        | GitHub OAuth Client ID          |
| GITHUB_SECRET    | GitHub OAuth Client Secret      |
| JWT_SECRET       | Secret key used for JWT signing |

---

# How to Configure Environment Variables

## macOS / Linux

Run:

```bash
export DB_NAME=your_database_name
export DB_USER_NAME=your_username
export DB_USER_PASSWORD=your_password
export GOOGLE_ID=your_google_client_id
export GOOGLE_SECRET=your_google_client_secret
export GITHUB_ID=your_github_client_id
export GITHUB_SECRET=your_github_client_secret
export JWT_SECRET=your_jwt_secret
```

---

## Windows CMD

Run:

```cmd
set DB_NAME=your_database_name
set DB_USER_NAME=your_username
set DB_USER_PASSWORD=your_password
set GOOGLE_ID=your_google_client_id
set GOOGLE_SECRET=your_google_client_secret
set GITHUB_ID=your_github_client_id
set GITHUB_SECRET=your_github_client_secret
set JWT_SECRET=your_jwt_secret
```

---

# Running the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

---

# Security Highlights

* Stateless authentication architecture
* JWT validation for all authenticated users
* OAuth2 + JWT combined authentication strategy
* Secret values externalized using environment variables
* Protected API endpoints
* Centralized authentication handling
* AOP-based request monitoring

---

# Learning Objectives

This project demonstrates:

* Enterprise authentication architecture
* JWT security implementation
* OAuth2 integration
* Unified authentication flow design
* Spring Security customization
* AOP implementation in real-world applications
* Secure secret management practices
* REST API protection strategies

---

# Future Improvements

Potential future enhancements:

* Refresh token implementation
* Redis token blacklist
* Email verification
* Multi-factor authentication (MFA)
* Docker support
* CI/CD pipeline integration
* Swagger/OpenAPI documentation
* Role and permission management enhancements

---

# Author

Muhammad Zain Ul Islam

Software Engineer | Spring Boot Backend Developer
