# Use an official lightweight Java runtime.
FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

# Copy the build jar (assuming Gradle created build/libs).
COPY build/libs/*.jar app.jar

# Make sure to set the right JVM memory flags for the Render free tier.
ENTRYPOINT ["java", "-jar", "/app.jar"]