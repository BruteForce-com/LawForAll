# LawForAll Backend

A Spring Boot application providing legal assistance with AI-powered chat functionality using Google's Gemini API.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Docker Setup](#docker-setup)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)

## Features

- JWT-based authentication and authorization
- AI-powered chat assistant using Google Gemini 2.0 Flash
- Session-based chat management
- PostgreSQL database with pgvector support
- RESTful API endpoints
- Docker containerization

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Security** with JWT authentication
- **Spring Data JPA** for data persistence
- **PostgreSQL** with pgvector extension
- **Spring AI** with Google Gemini integration
- **Maven** for dependency management
- **Docker & Docker Compose** for containerization

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher ([Download](https://adoptium.net/))
- **Maven 3.6+** (or use the included Maven Wrapper)
- **Docker & Docker Compose** ([Download](https://www.docker.com/products/docker-desktop))
- **PostgreSQL 14+** (if running without Docker)
- **Git** ([Download](https://git-scm.com/downloads))

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd LawForAll
```

### 2. Set Up Environment Variables

Create a `.env` file in the project root directory:

```bash
# Required: Google Gemini API Key
GEMINI_API_KEY=your_gemini_api_key_here

# For local development (application-dev.properties)
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=12345
```

**Note:** You can obtain a Gemini API key from [Google AI Studio](https://makersuite.google.com/app/apikey).

## Configuration

### Application Properties

The application uses different profiles for different environments:

- **Development**: `application-dev.properties` (local PostgreSQL)
- **Docker**: Configuration via environment variables in `docker-compose.yml`

### Key Configuration Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `GEMINI_API_KEY` | Google Gemini API key for AI chat | Required |
| `JWT_SECRET` | Secret key for JWT token signing | Provided default |
| `JWT_EXPIRATION_MS` | JWT token expiration time | 86400000 (24 hours) |
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/LawForAllDB` |

## Running the Application

### Option 1: Run Locally with Maven

**Step 1: Start PostgreSQL Database**

Ensure PostgreSQL is running on `localhost:5432` with a database named `LawForAllDB`.

**Step 2: Set Environment Variables**

```bash
# Windows (PowerShell)
$env:GEMINI_API_KEY="your_api_key"
$env:DATABASE_USERNAME="postgres"
$env:DATABASE_PASSWORD="12345"

# Linux/Mac
export GEMINI_API_KEY="your_api_key"
export DATABASE_USERNAME="postgres"
export DATABASE_PASSWORD="12345"
```

**Step 3: Run the Application**

```bash
# Using Maven Wrapper
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Or build and run JAR
./mvnw clean package
java -jar target/lawforall-backend.jar --spring.profiles.active=dev
```

The application will start on `http://localhost:8080`.

### Option 2: Run with Docker Compose (Recommended)

**Step 1: Ensure `.env` File is Configured**

Make sure your `.env` file contains at least:

```bash
GEMINI_API_KEY=your_gemini_api_key_here
```

**Step 2: Start All Services**

```bash
docker-compose up --build
```

This will:
- Build the Spring Boot application
- Start PostgreSQL with pgvector extension
- Start the API server on port 8080

**Step 3: Access the Application**

- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

**Step 4: Stop Services**

```bash
docker-compose down

# To remove volumes as well
docker-compose down -v
```

## Docker Setup

### Services

The application uses two Docker services:

1. **api** - Spring Boot application (port 8080)
2. **postgres** - PostgreSQL with pgvector (port 5432)

### Docker Commands

```bash
# Build and start services
docker-compose up --build

# Start services in detached mode
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild only the API service
docker-compose up --build api
```

## API Documentation

### Base URL

```
http://localhost:8080
```

### Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

### Key Endpoints

- **Authentication**
  - `POST /api/auth/register` - Register new user
  - `POST /api/auth/login` - Login and receive JWT token

- **Chat**
  - `POST /api/chat/ask` - Send message to AI assistant
  - `GET /api/chat/sessions` - Get all chat sessions
  - `GET /api/chat/sessions/{sessionId}` - Get chat messages for a session

- **Health Check**
  - `GET /actuator/health` - Application health status

## Project Structure

```
LawForAll/
├── src/
│   ├── main/
│   │   ├── java/com/bruteforce/lawforall/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── model/           # Entity models
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── security/        # Security configuration
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/                    # Unit and integration tests
├── .mvn/                        # Maven wrapper
├── docker-compose.yml           # Docker compose configuration
├── Dockerfile                   # Docker image definition
├── pom.xml                      # Maven dependencies
└── README.md                    # This file
```

## Troubleshooting

### Common Issues

**1. Database Connection Failed**

- Ensure PostgreSQL is running
- Verify database credentials in `.env` or `application-dev.properties`
- Check if the database `LawForAllDB` exists

**2. Port Already in Use**

If port 8080 or 5432 is already in use:

```bash
# Change ports in docker-compose.yml
ports:
  - "8081:8080"  # API
  - "5433:5432"  # PostgreSQL
```

**3. Gemini API Key Error**

- Verify your API key is correct
- Check API key has proper permissions
- Ensure environment variable is set correctly

**4. Docker Build Fails**

```bash
# Clean Docker cache and rebuild
docker-compose down -v
docker system prune -f
docker-compose up --build
```

## Development

### Running Tests

```bash
./mvnw test
```

### Building for Production

```bash
./mvnw clean package -DskipTests
```

The JAR file will be created at `target/lawforall-backend.jar`.

## Contributing

1. Create a feature branch (`git checkout -b feature/AmazingFeature`)
2. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
3. Push to the branch (`git push origin feature/AmazingFeature`)
4. Open a Pull Request

## License

This project is licensed under the terms specified in the LICENSE file.

## Contact

For questions or support, please open an issue in the repository.
