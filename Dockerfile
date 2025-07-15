#------Build the application--------
FROM gradle:8.7-jdk17 AS builder
COPY . /home/app
WORKDIR /home/app

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x generateJooq --no-daemon

#------Runtime image------------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]