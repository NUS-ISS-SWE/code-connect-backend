FROM azul/zulu-openjdk-alpine:21.0.6-21.40

WORKDIR /app

COPY target/codeconnect-interview-prep-service-0.0.1-SNAPSHOT.jar /app/codeconnect-interview-prep-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "codeconnect-interview-prep-service.jar"]