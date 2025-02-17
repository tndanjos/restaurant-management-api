# Build
FROM eclipse-temurin:23-jdk-alpine AS build

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

# Runtime
FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /app/target/restaurant-management-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
