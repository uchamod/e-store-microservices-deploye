# Multi-stage build for Spring Boot microservice
# Stage 1: Build stage
FROM maven:3.9-eclipse-temurin-21 AS build


WORKDIR /app


# Example: user-service/pom.xml, order-service/pom.xml, etc.
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B


COPY src ./src


# Add any service-specific build arguments here
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Install curl for ECS health checks to work
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app


# The JAR name should match your service's artifactId and version from pom.xml
# Format: target/service-name-version.jar
# Examples:
# - user-service-1.0.0.jar
# - order-service-1.0.0.jar
# - payment-service-1.0.0.jar
COPY --from=build /app/target/service_register-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring


# Service 1: 8081, Service 2: 8082, etc.
EXPOSE 8761


# These can also be set in ECS task definition
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"


# Update the port and path according to your service
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8761/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]