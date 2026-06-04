# Case tracker

A public-facing service written in Java. The public use the service to track civil appeal cases.

This is a legacy application maintained by the DTS Legacy team.

## Dependencies
- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Gradle 9.5.0](https://gradle.org/releases/#9.5.0)
- [Docker](https://www.docker.com)
- [PostgreSQL](https://www.postgresql.org/) (recommended via Docker for local development)
- [Node.js / npm](https://www.npmjs.com) (only required for frontend asset builds)

## Local development

The project no longer requires Ant for local development. It now uses the Gradle wrapper and Docker for build and runtime.

### Clone the repository

```
$ git clone git@github.com:ministryofjustice/hmcts-civil-appeal-case-tracker.git
$ cd hmcts-civil-appeal-case-tracker
```

### Build and package

Use the included helper script to build the WAR and Docker image:

```
$ ./build.sh
```

If you want to run the commands manually:

```
$ ./gradlew clean test war
$ docker build -t cact .
```

### Run the container

The Docker image runs Tomcat and serves the application on port `8080` inside the container.

```
$ docker run -p 80:8080 cact
```

The application will then be available at `http://localhost/8080`.

## Database setup

For local development, the easiest option is to run Postgres in Docker:

```
$ docker run --name case-tracker-db -e POSTGRES_USER=appuser -e POSTGRES_PASSWORD=apppassword -e POSTGRES_DB=case_tracker -p 5432:5432 -d postgres:14
```

If you prefer to install Postgres locally instead, download it from [postgresql.org](https://www.postgresql.org/download/).

If you need to connect the app to a database, pass database environment variables when starting the container:

```
$ docker run -e DB_HOST=... -e DB_PORT=... -e DB_USER=... -e DB_PASSWORD=... -p 80:8080 cact
```

### Helpful scripts

- `./build.sh` — runs `./gradlew clean test war` and then builds the Docker image.
- `./run-against-dev-db.sh` — helper script that pulls DB credentials from AWS ECS task definitions and starts the image.
- `./run-against-staging-db.sh` — similar helper for staging.

## Frontend asset build

If you are making frontend changes, install npm dependencies and use the existing Gulp setup:

```
$ npm install
```

Available Gulp commands:

| Command | Description |
|---|---|
| `gulp` | Build CSS and JS assets via default task |
| `gulp delete` | Delete compiled assets under `build/assets` |
| `gulp minify` | Minify CSS files into `build/assets` |
| `gulp uglify` | Minify JavaScript files into `build/assets` |
| `gulp lint` | Run JavaScript linting checks |
| `gulp watch` | Watch source CSS/JS and rebuild on changes |

## Notes

- The project builds a WAR file at `deploy/CACT.war` and the Docker image copies it into Tomcat.
- The Docker image is based on `tomcat:9.0.107-jdk11-temurin-jammy`.
- There is no need to install a local Tomcat instance for running the container.
