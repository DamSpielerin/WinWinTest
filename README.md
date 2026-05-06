# Mini Test: Two Spring Boot Apps with Postgres and Docker

Build two small Spring Boot apps (`auth-api` and `data-api`) that run via docker-compose.
`auth-api` handles simple auth with Postgres and exposes a client endpoint that calls `data-api`.
`data-api` processes the input and returns a result. 
`auth-api` saves a small record about the processed request.

## Requirements

- Docker and Docker Compose
- Optional: PostgreSQL, Maven and Java 17 if you want to build locally outside Docker

## Run

```bash
docker compose up -d --build
```

## Test The Flow

Register:

```bash
curl -i -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"a@a.com","password":"pass"}'
```

Login:

```bash
curl -i -X POST http://localhost:8080/api/auth/login\
 -H "Content-Type: application/json"\
 -d '{"email":"a@a.com","password":"pass"}'
```

Save the `token` value, then call the protected process endpoint:

```bash
curl -i -X POST http://localhost:8080/api/process \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"text":"hello"}'
```

Expected response:

```json
{"result":"OLLEH"}
```

Service A also writes one row to `processing_log`.

## Verify Service B Protection

Without the internal token, Service B rejects direct calls:

```bash
curl -i -X POST http://localhost:8081/api/transform \
  -H "Content-Type: application/json" \
  -d '{"text":"hello"}'
```

Expected: `403 Forbidden`.

With the shared internal token:

```bash
curl -i -X POST http://localhost:8081/api/transform \
  -H "Content-Type: application/json" \
  -H "X-Internal-Token: local-internal-token-change-me" \
  -d '{"text":"hello"}'
```

Expected: `{"result":"OLLEH"}`.

## Database Check

```postgresql
select user_id, input_text, output_text, created_at from processing_log order by created_at desc;
```

## Configuration

The .env file configures:

- `DB_NAME`
- `POSTGRES_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `INTERNAL_TOKEN`

For real deployments, create .env file with variables as at `.env.example`.
