# Weather App

This Java application utilizes the WeatherStack API to provide real-time weather data for the requested city. Simply input the city, and the program will get the current weather conditions

## Technologies
* Java 17
* Spring Boot
    * JPA Repository
    * Object Mapper
    * Rest Template
    * Caching
    * Logger
    * Validation
    * Resilience4j
* Lombok
* H2 Database
* Weatherstack API
* Open API


## Getting Started
To get started with this project, you will need to have the following installed on your local machine:

* JDK 14+
* Maven 3+

To build and run the project, follow these steps:

* Clone the repository: `https://github.com/azizCan10/WeatherApp.git`
* Navigate to the project directory
* in .env file
```env
API-KEY=YOUR_API_KEY
```
Put your Weatherstack API Key instead of "YOUR_API_KEY"
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run

-> The application will be available at http://localhost:8080