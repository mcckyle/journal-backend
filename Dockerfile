# ----------- Stage 1: Build the JAR file. -----------
FROM gradle:8-5-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# ----------- Stage 2: Run the JAR file. -----------
# Use an official lightweight Java runtime.
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the build jar (assuming Gradle created build/libs).
COPY jar from builder
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Make sure to set the right JVM memory flags for the Render free tier.
ENTRYPOINT ["java", "-Xmx512m", "-Dspring.profiles.active=prod", "-jar", "app.jar"]