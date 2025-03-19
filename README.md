# C2C Backend - Spring Boot  

This is the full stack app for the **C2C Web App**, built using **Spring Boot**, **MySQL**, and **Gradle** and **JSP** (Frontend)
## Deep diving into an older frontend stack of JSP (below is a video tutorial for my webapp demo) 
## I know Endpoints are still very buggy...as rerouting isnt handled properly thanks to the old JSP technology
https://github.com/user-attachments/assets/149e5968-f6ee-4436-bc70-5b1c06ecb598

### Prerequisites  

Ensure you have the following installed:  

- [Java 21](https://adoptopenjdk.net/)  
- [Gradle](https://gradle.org/install/)  
- [MySQL](https://dev.mysql.com/downloads/)  
- [Git](https://git-scm.com/)  

### 🔧 Setup Instructions  

#### 1️⃣ Clone the Repository  
```bash
git clone https://github.com/samyak2003-coder/C2C-Backend-Springboot.git
cd C2C-Backend-Springboot
```

#### 2️⃣ Configure MySQL Database
```bash
CREATE DATABASE ooad;
```

#### Add an environment file mentioning JWT_SECRET, format mentioned in .env.example

#### 3️⃣ Build and Run the Application
```bash
./gradlew build
./gradlew bootRun
```

#### Application URL (i have mentioned 8081 in application.properties cuz tomcats server was running, feel free to edit there)
http://localhost:8081


