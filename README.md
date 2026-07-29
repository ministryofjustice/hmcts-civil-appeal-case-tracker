# Case Tracker for Civil Appeals

A public-facing service that lets the public track civil appeal cases in the Court of Appeal,
Civil Division. Cases can be searched by hearing date, case number or title. An admin screen
allows a CSV of case data to be uploaded and imported, and a nightly job refreshes the data
from a published CSV.

Maintained by the DTS Legacy Support team.

Originally a Struts 1.x / Ant / Tomcat application, now migrated to Spring Boot.

## Tech stack

| | |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1 (embedded Tomcat, Spring MVC, Spring Security) |
| Views | Thymeleaf |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Build | Gradle (wrapper included) |
| Packaging | Executable jar (`build/libs/cact.jar`) on `eclipse-temurin:21-jre-jammy` |
| Deployment | Kubernetes on MOJ Cloud Platform, via CircleCI |

## Requirements

- JDK 21
- Docker (for a local Postgres, and for the integration tests)
- Node.js and Yarn, only if you are running the Playwright suites (see [TESTING.md](TESTING.md))

## Configuration

All configuration is supplied by environment variables. The defaults in `application.yaml` are
for local development only.

| Variable | Default         | Purpose |
|---|-----------------|---|
| `DB_HOST` | `localhost`     | Postgres host |
| `DB_PORT` | `5432`          | Postgres port |
| `DB_NAME` | `cact`          | Database name |
| `DB_USER` | `cact_user`     | Database user |
| `DB_PASSWORD` | `cact_password` | Database password |
| `ADMIN_USER` | `admin`         | Username for the admin screens |
| `ADMIN_PASS` | `pass`          | Password for the admin screens |
| `S3_BUCKET_NAME` | `test`          | Bucket holding `data.csv` for the nightly import |

In the deployed environments these come from Kubernetes secrets — see
`deploy_kubernetes/<env>/app-deployment.yaml`.

The schema is **not** managed by the application (`spring.jpa.hibernate.ddl-auto: validate`, and
there is no Flyway). It must exist before the app starts; the DDL is `files/setup.sql`.

## Running locally

Start a Postgres and apply the schema:

```bash
docker run -d --name cact-postgres -p 5433:5432 \
  -e POSTGRES_USER=cact_user -e POSTGRES_PASSWORD=cact_password -e POSTGRES_DB=cact \
  postgres:16
docker cp files/setup.sql cact-postgres:/setup.sql
docker exec cact-postgres psql -U cact_user -d cact -f /setup.sql
```

Run the app:

```bash
./gradlew bootRun
```

It starts on <http://localhost:8080>. To load some data, sign in at `/admin/login` with
`admin` / `pass` and upload `data.csv`.

## Building

```bash
./gradlew build          # compile, run unit tests, produce build/libs/cact.jar
./gradlew bootJar        # jar only
```

Docker image:

```bash
./gradlew bootJar
docker build -t civil-appeal-case-tracker .
```

## Testing

| Command | What it runs |
|---|---|
| `./gradlew test` | Unit tests. Integration tests (`*IT`) are excluded. |
| `./gradlew integrationTest` | Integration tests only. **Requires Docker** (Testcontainers). |
| `bin/smoke-test.sh` | Curl checks against a running instance (`BASE_URL`, default `http://localhost:8080`). |

`integrationTest` is not wired into `check`/`build`, so a normal build does not
need Docker.

For the Playwright end-to-end and accessibility suites, see [TESTING.md](TESTING.md).

## URLs

| Path | Purpose |
|---|---|
| `/` | Landing page |
| `/search` | Search form and results |
| `/case/{caseNo}` | Case detail |
| `/admin/login` | Admin sign-in |
| `/admin/upload` | Upload and import a CSV |
| `/admin/logout` | Sign out |
| `/health` | Actuator health — used by the Kubernetes probes |
| `/metrics` | Prometheus scrape endpoint |

The legacy Struts URLs (`/search.jsp`, `/search.do`, `/getDetail.do`, `/loginform.do`,
`/dumpData.do`, `/invalidate.do`) permanently redirect to their replacements, so existing
bookmarks and external links keep working.

## Behaviour worth knowing

- **Rate limiting** — non-browser traffic to `/search` and `/case/**` is limited to 20 requests
  per 60 seconds per IP, per pod (`app.rate-limit.*`). Browsers are never limited. Exceeding the
  limit returns 429.
- **Nightly import** — a scheduled job at 01:00 downloads `data.csv` from the S3 bucket over
  HTTPS and replaces the table contents. Each thread waits a random interval of up to an hour
  and skips the import if the data is already current, so threads do not duplicate the work.
- **Admin uploads** are imported directly from the request. A bad file produces a friendly
  message, and the existing data is left untouched.

## Monitoring

`/metrics` exposes Prometheus metrics. Alerting rules live in
`deploy_kubernetes/<env>/prometheus-custom-rules-case-tracker.yaml`:

- `application_exception_alert` — fires on 5xx responses (`http_server_requests_seconds_count`)
- `scheduled_task_exception_alert` — fires when the nightly import fails
  (`scheduled_task_exceptions_total`, from Micrometer's `@Counted`)

The scheduled job has no HTTP request, so the first alert cannot see it fail — which is why the
second one exists.

## CI/CD

CircleCI (`.circleci/config.yml`) builds the jar, runs the tests, builds the Docker images,
runs the smoke and Playwright suites, pushes to ECR and deploys with `kubectl`.

| Branch | Environment | Gate |
|---|---|---|
| `RST-*` | dev | automatic |
| `staging` | preprod | manual approval |
| `main` | prod | manual approval |
