# Cinema Server API

Backend for an online cinema ticket booking system. A complete REST API with full functionality for managing sessions, bookings, and Google Authentication integration.

[🇺🇸 English](README.md) | [🇺🇦 Українська](docs/README.uk.md)

## Key Features

- **Google Authentication** - secure login via Google One Tap Sign In
- **Movie Management** - full CRUD for movies, sessions, and halls
- **Booking System** - online seat reservation in cinema halls
- **Role-based Access** - USER, ADMIN, and WORKER roles support
- **Selenium Parsing** - ability to retrieve movie information by parsing data from UA Kino
- **REST API** - full-featured RESTful interface with validation

## Technology Stack

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Security** - OAuth2 Resource Server
- **Spring Data JPA / Hibernate** - ORM for database operations
- **PostgreSQL** - relational database
- **JWT (Auth0)** - authorization tokens
- **Selenium + JSoup** - web data parsing
- **Docker** - application containerization
- **Maven** - build system

## Functional Modules

### Authentication
- Login via Google ID Token
- JWT tokens with configurable lifetime
- Automatic role assignment based on email

### Movie Management
- Create, read, delete movies
- Parse information from UA Kino (title, genres, actors, posters, etc.)
- Store ratings (IMDB)
- Link movies to sessions

### Session System
- Create sessions linked to halls
- Automatic time availability checking
- Filter available sessions (min. 20 minutes before start)
- Count available seats

### Ticket Booking
- Reserve seats for authenticated and guest users
- Validate seat uniqueness
- Check session availability
- Store viewer information (email, phone, full name)

### Admin Panel
- Full management of movies, sessions, and halls
- Delete bookings
- View all sessions with details

## API Endpoints

### Public Endpoints
```
POST   /auth/google_id_token    - Google authentication
GET    /films/with_sessions     - Movies with available sessions
GET    /session/{id}            - Sessions for specific movie
GET    /session/available/by_film/{film_id} - Available sessions
GET    /hall/                   - List of cinema halls
POST   /booking/                - Create booking
```

### For Authenticated Users (USER)
```
GET    /users/                  - User profile
GET    /users/session/          - Sessions with user bookings
```

### For Administrators (ADMIN)
```
GET    /films/                  - All movies
POST   /films/                  - Create movie
DELETE /films/{id}              - Delete movie
GET    /films/uakino?request=   - Search movies on UA Kino
POST   /session/                - Create session
DELETE /session/{id}            - Delete session
POST   /hall/                   - Create hall
DELETE /hall/{id}               - Delete hall
DELETE /booking/{id}            - Delete booking
```

### For Workers (WORKER)
```
GET    /session/available_worker - Future sessions with full information
```

## Quick Start

### 1. Clone Repository
```bash
git clone https://github.com/AndriyKramar2288/cinema_server.git
cd cinema_server
```

### 2. Database Setup
Create a PostgreSQL database. The schema will be created automatically by Hibernate.

### 3. Application Configuration
Create the `src/main/resources/application.yaml` file based on the template provided in `application-example.yaml` in the same directory.
To do this, copy the entire contents of the example and specify values for the variables indicated by comments.

### 4. Obtain Google Client ID
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Create an OAuth 2.0 Client ID
4. Copy the Client ID to the corresponding parameter in `application.yaml`

### 5. Run Application

#### Via Maven Wrapper
```bash
./mvnw spring-boot:run
```

#### Build JAR
```bash
./mvnw clean package -DskipTests
java -jar target/cinema_server-0.0.1-SNAPSHOT.jar
```

The application will be available at: `http://localhost:8080`

## Deployment

This application, in addition to direct use of the `Dockerfile` and using the built image in appropriate environments, also provides the ability to use Docker Compose for comprehensive application launch.

### Build Image
```bash
docker build -t cinema-server:latest .
```

### Build and Run with Docker Compose

If using docker compose, follow these steps:

#### 1. Project Setup

Clone both repositories into one directory:

```bash
# Create working directory
mkdir cinema-app
cd cinema-app

# Clone backend
git clone https://github.com/AndriyKramar2288/cinema_server

# Clone frontend
git clone https://github.com/AndriyKramar2288/cinema_client

# Create directory for DB secrets
mkdir db
```

#### 2. Database Configuration

Create `db/password.txt` file with PostgreSQL password:

```bash
echo "your_secure_database_password" > db/password.txt
```

**Important**: Make sure `db/password.txt` is added to `.gitignore`!

#### 3. Create Configuration

Create `compose.yaml` file in the root directory:

```yaml
services:
  server:
    build:
      context: ./cinema_server
    ports:
      - 8080:8080
    secrets:
      - db-password
    depends_on:
      db:
        condition: service_healthy

  client:
    build:
      context: ./cinema_client
    environment:
      - NODE_ENV=production
      - REACT_APP_API_URL=http://localhost:8080
    ports:
      - 3000:3000
    depends_on:
      - server

  db:
    image: postgres:16-alpine
    restart: always
    user: postgres
    secrets:
      - db-password
    volumes:
      - cinema_volume_prod:/var/lib/postgresql/data
    environment:
      - POSTGRES_DB=cinema_db
      - POSTGRES_PASSWORD_FILE=/run/secrets/db-password
    expose:
      - 5432
    healthcheck:
      test: ["CMD", "pg_isready"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  cinema_volume_prod:

secrets:
  db-password:
    file: db/password.txt
```

#### 4. Run

```bash
# Build and start all services
docker compose up -d --build

# View logs
docker compose logs -f

# View logs for specific service
docker compose logs -f server
docker compose logs -f client
docker compose logs -f db
```

#### 5. Verify

After startup, check service availability:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080

#### 6. Management

```bash
# Stop services
docker compose down

# Stop with volume removal (WARNING: will delete DB!)
docker compose down -v

# Restart specific service
docker compose restart server

# View status
docker compose ps

# Connect to DB
docker compose exec db psql -U postgres -d cinema_db
```

## Application Architecture

```
src/main/java/com/banew/cinema_server/
├── backend/
│   ├── configs/           # Security, CORS, Jackson
│   ├── controllers/       # REST controllers
│   ├── dto/              # Data Transfer Objects
│   ├── entities/         # JPA entities
│   ├── repositories/     # Spring Data repositories
│   ├── services/         # Business logic
│   └── exceptions/       # Custom exceptions
```

### Main Entities
- **CinemaUser** - system users
- **Film** - movies with metadata
- **ViewSession** - cinema sessions
- **Hall** - cinema halls
- **Booking** - bookings
- **CinemaViewer** - viewer information

## Security

- JWT tokens with HMAC256 signature
- OAuth2 Resource Server
- Google ID token validation
- Role-based access (ROLE_USER, ROLE_ADMIN, ROLE_WORKER)
- CORS configuration
- Input data validation

## Movie Parsing

The system uses Selenium WebDriver for dynamic UA Kino parsing:
- Automatic movie search
- Retrieve detailed information (actors, director, genres)
- Download posters and stills
- Parse IMDB ratings

**Note**: Google Chrome is installed in the Docker image for Selenium.