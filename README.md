# recipe-management-app

Android application for managing recipes, ingredients, and shopping lists.

## Project Structure

* `/frontend` — Angular web application (IKS)

* `/backend` — Java Spring Boot REST API & PostgreSQL database integration (ISS)

## Prerequisites

* **Java:** JDK 17 or higher

* **Maven:** 3.8+ (or included Maven Wrapper `./mvnw`)

* **Database:** PostgreSQL 15+

* **Node.js & Angular CLI:** Node 18+ / Angular CLI

## Getting Started

### 1. Backend Setup (Spring Boot + PostgreSQL)

1. Open **pgAdmin 4** (or `psql`) and create a new PostgreSQL database named `recipes_db`:

   ```
   CREATE DATABASE recipes_db;
   
   ```

2. Navigate to `backend/src/main/resources/application.properties` and update your local PostgreSQL credentials if needed:

   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/recipes_db
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_POSTGRES_PASSWORD
   
   ```

3. Run the backend application from IntelliJ IDEA (`RecipesBackendApplication.java`) or via terminal:

   ```
   cd backend
   ./mvnw spring-boot:run
   
   ```

4. The server will start on `http://localhost:8080` and automatically seed initial lookup data (meal types, dish types, diet types, ingredient categories, allergens, and a default admin account).

   * Health check endpoint: `GET http://localhost:8080/api/health`

   * Meal types endpoint: `GET http://localhost:8080/api/meal-types`

### 2. Frontend Setup (Angular)

1. Navigate to the frontend directory and install dependencies:

   ```
   cd frontend
   npm install
   
   ```

2. Start the development server:

   ```
   ng serve
   
   ```

3. Open `http://localhost:4200` in your browser.

## Team Members (Team Bosanci)

* **Nemanja Rikić** (Student 1)

* **Kristian Zhou-Gubić** (Student 2)

* **Miloš Vukić** (Student 3)