# Stage 1: Build
FROM gradle:7.6.2-jdk17 AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle build --no-daemon

# Stage 2: Run
FROM openjdk:17-jdk-slim
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
