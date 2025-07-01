FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

COPY target/*.jar sports-tournament-organiser.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "sports-tournament-organiser.jar"]
