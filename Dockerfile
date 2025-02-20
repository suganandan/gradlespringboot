# Use a multi-stage build to reduce the final image size

# Stage 1: Build the Gradle application
FROM gradle:7.6.2-jdk17 AS build  # Or gradle:7.6.2 and manage JDK in build.gradle

WORKDIR /home/gradle/src

COPY . /home/gradle/src  # Copy the entire project

# Pre-cache dependencies (optional but recommended)
RUN gradle dependencies --no-daemon

# Build the application (including Jib if you're still using it - otherwise just 'build')
RUN gradle clean build

# Stage 2: Create the final image (smaller and more efficient)
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy ONLY the built JAR file from the build stage
COPY --from=build /home/gradle/src/build/libs/gradlespringboot-0.0.1-SNAPSHOT.jar /app/gradlespringboot.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/gradlespringboot.jar"]