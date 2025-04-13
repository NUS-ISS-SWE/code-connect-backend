FROM azul/zulu-openjdk-alpine:21.0.6-21.40

WORKDIR /app

RUN apk add --no-cache bash curl

RUN curl -o /wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
    && chmod +x /wait-for-it.sh

COPY target/codeconnect-liquibase-service-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["/wait-for-it.sh", "postgres-db:5432", "--timeout=60", "--", "java", "-jar", "app.jar"]