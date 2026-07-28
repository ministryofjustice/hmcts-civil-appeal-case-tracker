FROM eclipse-temurin:21-jre-jammy

RUN adduser --disabled-password app -u 1001 \
    && mkdir -p /app/uploads /app/logs \
    && chown -R 1001:1001 /app


COPY --chown=1001:1001 build/libs/cact.jar /app/

USER 1001
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/cact.jar"]
