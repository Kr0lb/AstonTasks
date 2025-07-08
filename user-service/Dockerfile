FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]