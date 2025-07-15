#------Build the application--------
FROM gradle:8.7-jdk17 AS builder

WORKDIR /home/app
COPY . .


COPY docker/wait-for.sh /wait-for.sh
RUN chmod +x /wait-for.sh
ENV JOOQ_JDBC_URL=jdbc:postgresql://db:5432/prewave_edge_tree_db \
    JOOQ_JDBC_USER=postgres \
    JOOQ_JDBC_PASSWORD=postgres123
RUN /wait-for.sh db:5432 -- ./gradlew clean build --no-daemon

#------Runtime image------------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]