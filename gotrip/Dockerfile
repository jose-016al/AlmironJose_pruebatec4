FROM openjdk:17-jdk-slim
ARG JAR_FILE=gotrip-0.0.1.jar
COPY ${JAR_FILE} gotrip.jar
ENTRYPOINT ["java", "-jar", "gotrip.jar"]