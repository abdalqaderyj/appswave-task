# Backend Assignment - News Management System

## Overview
The primary goal of this project is to implement the backend and API layers for a news management system,
providing authentication, user management, and news management capabilities.
The project adheres to API design best practices and includes features like JWT-based authentication,
role-based access control, and a refresh token mechanism.

## Prerequisites
- Java 11
- Maven
- MySql
- IDE (preferred intellij)

## Setup
- git clone <repository-url>
- Update `application.properties` with your MySQL credentials
- Build the Application run `mvn clean install`
- Run the Application `mvn spring-boot:run`

## Authentication APIs
| **Endpoint**                  | **Method** | **Description**                       | **Authentication** |
|--------------------------------|------------|---------------------------------------|---------------------|
| `/v1/appswave-api/auth/signup` | `POST`     | Register a new user                   | Public              |
| `/v1/appswave-api/auth/login`  | `POST`     | Log in and get `accessToken` and `refreshToken` | Public        |
| `/v1/appswave-api/auth/refresh-token` | `POST` | Generate a new `accessToken` using a `refreshToken` | Public |
| `/v1/appswave-api/auth/logout` | `POST`     | Log out and revoke the `refreshToken` | Public              |

## User Management APIs
| **Endpoint**                       | **Method** | **Description**                 | **Authentication** |
|------------------------------------|------------|---------------------------------|---------------------|
| `/v1/appswave-api/user`            | `PUT`      | Update user details             | Bearer Token        |
| `/v1/appswave-api/user/all`        | `GET`      | Fetch all users                 | Public        |
| `/v1/appswave-api/user/{{id}}`     | `GET`      | Fetch user by ID                | Public        |
| `/v1/appswave-api/user/{{id}}`     | `DELETE`   | Delete user by ID               | Bearer Token        |

## News Management APIs
| **Endpoint**                                 | **Method** | **Description**                                 | **Authentication** |
|----------------------------------------------|------------|-----------------------------------------------|---------------------|
| `/v1/appswave-api/news/{{status}}/all`       | `GET`      | Fetch all news by status (e.g., approved/all) | Bearer Token        |
| `/v1/appswave-api/news`                      | `POST`     | Create news                                   | Bearer Token        |
| `/v1/appswave-api/news`                      | `PUT`      | Update existing news                          | Bearer Token        |
| `/v1/appswave-api/news/{{id}}`               | `GET`      | Fetch news by ID                              | Bearer Token        |
| `/v1/appswave-api/news/{{id}}`               | `DELETE`   | Delete news                                   | Bearer Token        |
| `/v1/appswave-api/news/{{id}}/approve`       | `PUT`      | Approve news (Admin only)                     | Bearer Token        |
