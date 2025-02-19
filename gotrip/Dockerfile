# Etapa de construcción (compila el proyecto)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa final (ejecuta el `.jar`)
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/gotrip-0.0.1.jar gotrip.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gotrip.jar", "--server.port=${PORT}"]