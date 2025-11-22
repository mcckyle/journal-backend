![Build Status](https://github.com/mcckyle/journal-backend/actions/workflows/journal-tests-ci.yml/badge.svg)

# Gratitude Journal Backend

## Overview
The Gratitude Journal backend is a RESTful web service built with **Java Spring Boot**. It provides the backend functionality for the **Gratitude Journal** web application, enabling users to reflect on their daily moments of gratitude. The backend features secure user authentication, the ability to create, read, update, and delete journal entries, and personalized insights based on user data.

## Features
- **Secure User Authentication**: Users can sign up, log in, and manage their accounts using **JWT (JSON Web Tokens)**.
- **CRUD Operations for Journal Entries**: Users can create, read, update, and delete their gratitude journal entries.
- **Calendar View Integration**: A calendar-based view for users to track their entries over time.
- **Personalized Insights**: Based on the user's journal entries, the system provides insights to encourage positive habits.

## Technologies Used
- **Spring Boot**: Backend framework to build the REST API.
- **Spring Security**: For handling user authentication and authorization.
- **MariaDB**: A relational database to store user data and journal entries.
- **JPA / Hibernate**: Used to interact with the MariaDB database.
- **JWT (JSON Web Tokens)**: Used for secure authentication.

## Installation

### Prerequisites
- Java 11 or higher
- Gradle 6.0 or higher
- MariaDB server (or any other compatible relational database)
- Postman or another API testing tool (optional)

### Clone the Repository
Clone the backend repository to your local machine.

```bash
git clone https://github.com/your-username/journal-backend.git
```

### Set Up the Database
1. Install MariaDB or use an existing instance.
2. Create a database for the application, for example:

```sql
CREATE DATABASE gratitude_journal;
```

3. Update your `application.properties` file with the correct MariaDB connection details:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/gratitude_journal
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
```

### Run the Application
You can run the Spring Boot application using Gradle:

```bash
./gradlew bootRun
```

The backend will start running on `http://localhost:8080` by default.

### Testing the Application
To test the endpoints, you can use Postman or any API testing tool. The following endpoints are available:

- **POST /api/auth/signup**: Create a new user account.
- **POST /api/auth/login**: Authenticate and receive a JWT token.
- **GET /api/entries**: Get a list of all journal entries for the authenticated user.
- **POST /api/entries**: Create a new journal entry.
- **PUT /api/entries/{id}**: Update an existing journal entry.
- **DELETE /api/entries/{id}**: Delete a journal entry.

### Example Request: Create a New Journal Entry
**Endpoint**: `POST /api/entries`

**Request Body**:

```json
{
  "title": "A Moment of Gratitude",
  "content": "Today, I'm grateful for my supportive friends."
}
```

**Response**:

```json
{
  "id": 1,
  "title": "A Moment of Gratitude",
  "content": "Today, I'm grateful for my supportive friends.",
  "date": "2024-12-06T12:00:00"
}
```

## Running Unit Tests
The project includes unit and integration tests to ensure functionality. You can run tests using Gradle:

```bash
./gradlew test
```

## Deployment

For production deployment, you can package the backend application as a Docker container or deploy it to a cloud service like AWS, Heroku, or DigitalOcean.

### Example Docker Setup
1. Create a Dockerfile in the project root:

```dockerfile
FROM openjdk:11-jre-slim
COPY build/libs/gratitude-journal-backend.jar /app/gratitude-journal-backend.jar
ENTRYPOINT ["java", "-jar", "/app/gratitude-journal-backend.jar"]
```

2. Build the Docker image:

```bash
docker build -t gratitude-journal-backend .
```

3. Run the Docker container:

```bash
docker run -p 8080:8080 gratitude-journal-backend
```

## Contribution

Feel free to fork the repository, make improvements, and create pull requests. Issues and feature requests are welcome.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Note:** This is the backend API for the Gratitude Journal application. For the frontend React application, please refer to the corresponding repository.
