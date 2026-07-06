#Case tracker

A public facing service written in Java. The public use the service to track civil appeal cases.

This is a legacy application maintained by Tactical Products team.




## Dependencies
- [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [Tomcat 9](https://hub.docker.com/_/tomcat?tag=jre21-temurin-jammy)
- [Ant](http://ant.apache.org/bindownload.cgi)

### For local development

- Docker
- Postgres14 (recommended via Docker for local development)

### For frontend
- [NPM 4.0+](https://www.npmjs.com)
- [Gulp.js](http://www.gulpjs.com)

## Setting up

Clone this repository

```
$ git clone git@github.com:ministryofjustice/hmcts-civil-appeal-case-tracker.git
```

Next change the directory to the new project

```
$ cd hmcts-civil-appeal-case-tracker.git
```

From the root, run this command to build the project via Ant docker container, then build the Case Tracker docker image.

```
./build.sh
```

If you need to connect the app to a database, pass database environment variables when starting the container:

```
$ docker run -e DB_HOST=... -e DB_PORT=... -e DB_USER=... -e DB_PASSWORD=... -p 80:8080 cact
```

### Front-end development

Install Node js (version 4) using homebrew

First install and setup Gulp and the dependencies

```
$ npm install
```
This should setup Gulp and all its tasks


|   Command	|  Description 	|
|---	|---	|
| gulp | Runs the default taks which performs all the below tasks in the order they appear below(except lint)|
| gulp delete  	| Deletes the build/assets folder i.e the compiled CSS and JS. 	|
| gulp minify  	| Minifies all the css files and addes them to the build/assets folder  	|
|   gulp uglify	|  Minifies all the Javascript files and addes them to the build/assets folder 	|
| gulp lint  	| Checkes the Javascript to ensure the it meets specific rules  	|
| gulp watch  	| Whats the CSS and JS source folders and if any thing changes it runs either minify or uglify tasks 	|
|   	|   	|
