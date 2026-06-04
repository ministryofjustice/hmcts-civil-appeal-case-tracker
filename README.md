#Case tracker

A public facing service written in Java. The public use the service to track civil appeal cases.

This is a legacy application maintained by Tactical Products team.




##Dependencies
- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Tomcat 9](https://hub.docker.com/layers/library/tomcat/9.0.107-jdk11-temurin-jammy/images/sha256-2fc302f23c98fbd06cc69d9bf35a486fff0b43ab03bbc68906cf8825ff51c6f0)
- [Gradle 9.5.0](https://gradle.org/releases/#9.5.0)

###For local development

- Docker
- Windows 8 or 10
- SQL Server 2012 Express

###For frontend
- [NPM 4.0+](https://www.npmjs.com)
- [Gulp.js](http://www.gulpjs.com)

##Setting up

###Install Ant
Ant can be installed quiet easily using [Homebrew](http://brew.sh/)

```
$ brew install ant
```
Check ant has installed properly by running ```ant```. If you see the following after running ```ant``` then you know it has been installed successfully

```
$ ant
Buildfile: build.xml does not exist!
Build failed
```
####Setup environment variables for build process

You will need the following information to substitue in the scripts below :

- **/path/to/tomcat**  path to you systems Tomcat installation 
- **connection-string** is the connection string to your database and tables for the service to use
- **connection-username** is the database username
- **connection-password** is the database users password

```
$ echo 'export TOMCAT_PATH="/path/to/tomcat"' >>~/.bash_profile
$ echo 'export DEV_CASE_TRACKER_URL="connection-string"' >> ~/.bash_profile
$ echo 'export DEV_CASE_TRACKER_USER="connection-username"' >> ~/.bash_profile
$ echo 'export DEV_CASE_TRACKER_PWD="connection-password"' >> ~/.bash_profile
```

If you plan on deploying to staging and production you will need to run the following:

```
$ echo 'export STAGE_CASE_TRACKER_URL="connection-string"' >> ~/.bash_profile
$ echo 'export STAGE_CASE_TRACKER_USER="connection-username"' >> ~/.bash_profile
$ echo 'export STAGE_CASE_TRACKER_PWD="connection-password"' >> ~/.bash_profile

$ echo 'export LIVE_CASE_TRACKER_URL="connection-string"' >> ~/.bash_profile
$ echo 'export LIVE_CASE_TRACKER_USER="connection-username"' >> ~/.bash_profile
$ echo 'export LIVE_CASE_TRACKER_PWD="connection-password"' >> ~/.bash_profile

```

Now run it so its available in your current terminal

```
$ source ~/.bash_profile
```

###Get the source code
Clone this repository

```
$ git clone git@github.com:ministryofjustice/hmcts-civil-appeal-case-tracker.git
```

Next change the directory to the new project

```
$ cd hmcts-civil-appeal-case-tracker.git
```

###Front-end development

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
