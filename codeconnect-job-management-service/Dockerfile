FROM azul/zulu-openjdk-alpine:21-latest

WORKDIR /app

COPY target/codeconnect-job-management-service-0.0.1-SNAPSHOT.jar /app/codeconnect-job-management-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "codeconnect-job-management-service.jar"]