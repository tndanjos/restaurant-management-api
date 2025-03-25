# Build
FROM eclipse-temurin:23-jdk-alpine AS build

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

# Runtime
FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

RUN apk add --no-cache bash git curl

COPY --from=build /app /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/restaurant-management-api-0.0.1-SNAPSHOT.jar"]
