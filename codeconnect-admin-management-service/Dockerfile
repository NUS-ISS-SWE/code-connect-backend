FROM azul/zulu-openjdk-alpine:21-latest

WORKDIR /app

COPY target/codeconnect-admin-management-service-*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]