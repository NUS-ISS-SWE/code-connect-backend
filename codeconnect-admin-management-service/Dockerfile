FROM azul/zulu-openjdk-alpine:21.0.6-21.40

WORKDIR /app

COPY target/codeconnect-admin-management-service-*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]