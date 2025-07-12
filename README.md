# Spring Boot JWT Security Template

A robust and secure Spring Boot template for implementing JWT (JSON Web Token) based authentication and authorization. This project provides a solid foundation for building RESTful APIs with modern security practices, including refresh token rotation, token blacklisting with Redis, and comprehensive error handling.

## ✨ Features

*   **JWT-based Authentication:** Secure user registration and login with access and refresh tokens.
*   **Refresh Token Rotation:** Enhances security by invalidating the old refresh token and issuing a new one upon each successful refresh request, significantly reducing the risk of token compromise.
*   **Hashed Refresh Tokens:** Refresh tokens are securely hashed before storage in the database, protecting against database breaches.
*   **Redis-backed Token Blacklisting:** Utilizes Redis for efficient and fast blacklisting of invalidated access tokens (e.g., on logout), preventing their reuse.
*   **Role-Based Authorization:** Supports user roles for granular access control.
*   **Global Exception Handling:** Centralized error handling with custom exceptions and structured JSON error responses for a consistent API experience.
*   **Input Validation:** Robust validation of incoming request DTOs using `jakarta.validation` to ensure data integrity and prevent invalid input.
*   **Service Layer Abstraction:** Uses interfaces for service implementations, promoting modularity, testability, and adherence to SOLID principles.
*   **Externalized Configuration:** All sensitive and environment-specific configurations (JWT secrets, token expiration times, database credentials) are externalized for easy management.

## 🚀 Technologies Used

*   **Spring Boot 3.x:** The core framework for building the application.
*   **Spring Security:** For authentication and authorization.
*   **JWT (JSON Web Tokens):** For secure, stateless authentication.
*   **Spring Data JPA:** For database interaction.
*   **MySQL:** Relational database for user and refresh token storage.
*   **Redis:** In-memory data store for JWT blacklisting.
*   **Lombok:** Reduces boilerplate code.
*   **Maven:** Dependency management and build automation.
*   **Dotenv-Java:** For loading environment variables from `.env` files.
*   **Jakarta Validation:** For declarative input validation.

## 🛠️ Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

*   **Java Development Kit (JDK) 21** or higher
*   **Apache Maven 3.x**
*   **MySQL Server**
*   **Redis Server**

### 1. Clone the Repository

```bash
git clone https://github.com/Anss1/JWT_Authentication-Refresh_Token-Redis_Blacklist.git
```

### 2. Database Setup

Create a MySQL database for the application. You can name it `jwt_security_db` or anything you prefer.

```sql
CREATE DATABASE IF NOT EXISTS jwt_security_db;
```

The application will use Spring Data JPA to automatically create the necessary tables (`users`, `refresh_tokens`) on startup.

### 3. Environment Variables

Create a `.env` file in the root directory of the project (same level as `pom.xml`) and add your JWT secret key. This key should be a strong, random string.

```dotenv
JWT_SECRET_KEY=YourSuperSecretJWTKeyThatIsAtLeast256BitsLongAndRandomlyGenerated
```

### 4. Application Properties

Configure your database and Redis connection details in `src/main/resources/application.properties`.

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/jwt_security_db?createDatabaseIfNotExist=true
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update # Use 'create' or 'create-drop' for fresh setup, 'update' for existing
spring.jpa.show-sql=true

# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT Token Expiration (in milliseconds)
jwt.access-token.expiration-ms=900000 # 15 minutes
jwt.refresh-token.expiration-ms=604800000 # 7 days
```

**Note:** Ensure your Redis server is running and accessible at the configured host and port.

### 5. Build the Project

```bash
mvn clean install
```

### 6. Run the Application

```bash
mvn spring-boot:run
```
### 7. Test the Application
In Linux: If you have already postman installed just open terminal and simpley type postman
```bash
postman
```
if not, you can instal it via
```bash
sudo snap install postman
```

The application will start on `http://localhost:8080` (or your configured port).

## 💡 API Endpoints

The following are the primary authentication endpoints:

*   **`POST /auth/register`**: Register a new user.
    *   Request Body: `RegisterRequest` (firstName, lastName, email, password, role)
    *   Response: `AuthResponse` (accessToken, refreshToken)
*   **`POST /auth/login`**: Authenticate a user and get tokens.
    *   Request Body: `AuthRequest` (email, password)
    *   Response: `AuthResponse` (accessToken, refreshToken)
*   **`POST /auth/refresh`**: Obtain a new access token using a refresh token.
    *   Request Body: `RefreshRequest` (refreshToken)
    *   Response: `AuthResponse` (newAccessToken, newRefreshToken)
*   **`POST /auth/logout`**: Invalidate the current access token and revoke the refresh token.
    *   Requires `Authorization: Bearer <accessToken>` header.
    *   Response: `String` message

## 🤝 Contributing

Feel free to fork the repository, open issues, and submit pull requests.
