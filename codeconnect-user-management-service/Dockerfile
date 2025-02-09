FROM openjdk:21
WORKDIR /app
COPY target/codeconnect-user-management-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]