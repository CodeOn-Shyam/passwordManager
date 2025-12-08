# Password Manager - Spring Boot, AES Encryption, PostgreSQL, Fly.io Deployment

This project is a secure Password Manager backend developed using Spring Boot. It provides encrypted credential storage, user authentication, and secure access to stored passwords. The application follows a layered architecture (Controller → Service → Repository) and uses PostgreSQL for data persistence.

## Features

- User registration and login using Spring Security (Basic Auth)
- AES-256 encryption for stored passwords
- BCrypt hashing for user login passwords
- CRUD operations for credentials
- Password reveal endpoint accessible only by authenticated users
- Persistent storage using PostgreSQL
- Clean DTO-based response structure
- Global error handling
- Ready for future enhancements such as MFA and password generator

## Technologies Used

| Component | Technology |
|-----------|------------|
| Backend | Spring Boot 3, Java 17 |
| Security | Spring Security, BCrypt |
| Encryption | AES-256 |
| Database | PostgreSQL |
| ORM | Spring Data JPA |
| Build System | Maven |
| Deployment | Fly.io with Docker |
| Testing | Postman / HTTP Client |
| Dev DB | H2 (fallback for local use) |

## Project Structure

```

src/
└─ main/
├─ java/org.codeon.passwordmanager/
│   ├─ controller/
│   ├─ service/
│   ├─ repository/
│   ├─ model/
│   ├─ dto/
│   ├─ security/
│   └─ util/
└─ resources/
├─ static/
└─ application.properties

```

## Running Locally

### Prerequisites
- Java 17 or later
- Maven
- Postman or any HTTP client

### Start the application

```

./mvnw spring-boot:run

```

### Example API Endpoints

Register a user:
```

POST /auth/register

```

Add a credential (Basic Auth required):
```

POST /api/credentials

```

Get all user credentials:
```

GET /api/credentials

```

Reveal password for a credential:
```

GET /api/credentials/{id}/reveal

```

## Deploying on Fly.io with PostgreSQL

### Create Fly app

```

fly launch

```

### Create PostgreSQL instance

```

fly postgres create --name passwordmanager-db

```

### Configure secrets

```

fly secrets set 
SPRING_DATASOURCE_URL="jdbc:postgresql://passwordmanager-db.internal:5432/passwordmanager_db" 
SPRING_DATASOURCE_USERNAME="passwordmanager_db_user" 
SPRING_DATASOURCE_PASSWORD="your_db_password" 
SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"

```

### Deploy the application

```

fly deploy

```

Live application endpoint example:
```

[https://your-app-name.fly.dev](https://your-app-name.fly.dev)

```

## Environment Variables

| Variable | Description |
|----------|-------------|
| SPRING_DATASOURCE_URL | JDBC connection URL |
| SPRING_DATASOURCE_USERNAME | Database username |
| SPRING_DATASOURCE_PASSWORD | Database password |
| SPRING_JPA_DATABASE_PLATFORM | Hibernate dialect |
| PORT | Server port from Fly.io |

## Planned Enhancements

- Multi-Factor Authentication (OTP and Google Authenticator)
- Password reveal using verification key
- Forgot password and forgot key flows
- Login and register using Google OAuth
- Password strength analyzer and generator
- Frontend UI using HTML, CSS, JS or React
- Microservice architecture

## Contributing

Contributions are welcome. For significant changes, open an issue first to discuss what you would like to modify.

## License

MIT License
