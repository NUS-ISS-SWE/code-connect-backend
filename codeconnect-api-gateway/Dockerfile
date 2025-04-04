FROM azul/zulu-openjdk-alpine:21-latest

WORKDIR /app

COPY target/codeconnect-api-gateway-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
