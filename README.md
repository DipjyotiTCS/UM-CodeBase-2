# User Management Module (Spring Boot 2.5.11, Java 8, PostgreSQL)

## APIs
- `POST /api/auth/register` - register user
- `POST /api/auth/login` - authenticate and receive JWT
- `GET /api/auth/validate` - validate JWT
- `GET /api/users/me` - secured sample endpoint

## Run (dev)
1) Start PostgreSQL (or use docker-compose below)
2) Update `src/main/resources/application.yml` dev profile if needed
3) Run:
```bash
mvn spring-boot:run
```

## Run (prod)
Uses environment variables + Flyway migrations:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `SPRING_PROFILES_ACTIVE=prod`

Example:
```bash
SPRING_PROFILES_ACTIVE=prod DB_URL=jdbc:postgresql://localhost:5432/user_management DB_USERNAME=postgres DB_PASSWORD=postgres mvn spring-boot:run
```

## Docker compose (local postgres)
```bash
docker compose up -d
```

## Notes
- Replace `app.security.jwt.secret` with a strong secret and store securely.
- `prod` profile uses `ddl-auto=validate` and Flyway migrations under `src/main/resources/db/migration`.
