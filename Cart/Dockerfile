# Multi-stage build for Spring Boot microservice
# Stage 1: Build stage
FROM maven:3.9-eclipse-temurin-21 AS build

# ⚠️ CHANGE THIS: Set working directory (can keep same for all services)
WORKDIR /app

# ⚠️ CHANGE THIS: Copy pom.xml for your specific service
# Example: user-service/pom.xml, order-service/pom.xml, etc.
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# ⚠️ CHANGE THIS: Copy source code for your specific service
COPY src ./src

# ⚠️ CHANGE THIS: Build the application (skip tests for faster builds)
# Add any service-specific build arguments here
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# ⚠️ CHANGE THIS: Copy the JAR file from build stage
# The JAR name should match your service's artifactId and version from pom.xml
# Format: target/service-name-version.jar
# Examples:
# - user-service-1.0.0.jar
# - order-service-1.0.0.jar
# - payment-service-1.0.0.jar
COPY --from=build /app/target/Cart-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# ⚠️ CHANGE THIS (OPTIONAL): Expose port - should match your service's port
# Service 1: 8081, Service 2: 8082, etc.
EXPOSE 8082

# ⚠️ CHANGE THIS (OPTIONAL): Add service-specific environment variables
# These can also be set in ECS task definition
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# ⚠️ OPTIONAL: Add health check
# Update the port and path according to your service
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]