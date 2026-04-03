# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml and dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine

## Set environnement JAVA
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="$PATH:$JAVA_HOME/bin"

## create non root user and group
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /opt

# Copy the built artifact from the build stage
# FinalName in pom.xml is sepa26server
COPY --from=build /app/target/sepa26server.war /opt/sepa26server.war

## Set the nonroot user as default user
USER spring:spring

# Active le profil docker qui contient la config MongoDB correcte
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/opt/sepa26server.war"]

## Expose the port
EXPOSE 8100
