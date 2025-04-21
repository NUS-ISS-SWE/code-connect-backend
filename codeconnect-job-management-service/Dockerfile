FROM azul/zulu-openjdk-alpine:21.0.6-21.40

WORKDIR /app

COPY target/codeconnect-job-management-service-0.0.1-SNAPSHOT.jar /app/codeconnect-job-management-service.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "codeconnect-job-management-service.jar"]