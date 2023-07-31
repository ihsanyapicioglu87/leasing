# Allane SE Leasing Application

This is the README file for the Allane SE Leasing Application, which fulfills the requirements for the code challenge. 
This application is a backend REST service built using Java 11 and Spring Boot 2.5 to manage leasing contracts, customers, and vehicles etc. 
It persists the data into a MySQL database that is locally dockerized. 
The application is built and managed using Gradle, and Flyway database migration is used to create the initial schema. 

## Preconditions

Before running the application, please ensure the following prerequisites are met:

1. Java 11 is installed on your system.
2. Docker is installed and running on your system for the local database.
3. Gradle is installed on your system for building and managing the project.

## How to Start the Application

Follow the steps below to set up and run the Allane SE Leasing Application:

1. **Clone the Git repository:**
   ```bash
   git clone https://github.com/ihsanyapicioglu87/leasing.git
   cd leasing

docker run -d -p 3306:3306 --name leasingDB -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=leasing mysql

./gradlew build
java -jar build/libs/leasing-application.jar


## The application will be accessible at: 
http://localhost:8080
