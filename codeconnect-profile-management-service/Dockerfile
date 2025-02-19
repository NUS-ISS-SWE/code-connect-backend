FROM azul/zulu-openjdk-alpine:21-latest

WORKDIR /app

COPY target/codeconnect-profile-management-service-0.0.1-SNAPSHOT.jar /app/codeconnect-profile-management-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "codeconnect-profile-management-service.jar"]