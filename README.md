C2C Backend - Spring Boot
This is the backend for the C2C Web App, built using Spring Boot, MySQL, and Gradle.

🚀 Getting Started
Prerequisites
Ensure you have the following installed:

Java 21
Gradle
MySQL
Git
🔧 Setup Instructions
Clone the Repository

bash
Copy
Edit
git clone https://github.com/samyak2003-coder/C2C-Backend-Springboot.git
cd C2C-Backend-Springboot
Configure MySQL Database

Create a MySQL database:

sql
Copy
Edit
CREATE DATABASE ooad;
Ensure MySQL is running and update the database credentials in src/main/resources/application.properties:

ini
Copy
Edit
spring.datasource.url=jdbc:mysql://localhost:3306/ooad
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
Build and Run the Application

Using Gradle:

bash
Copy
Edit
./gradlew build
./gradlew bootRun

bash
Copy
Edit
./gradlew bootJar
java -jar build/libs/C2C-Backend-Springboot.jar
Access the API
Once the application starts, access it at:
http://localhost:8080

