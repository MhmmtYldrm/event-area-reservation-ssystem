FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean package -DskipTests \
    && echo "=== TARGET CONTENT ===" \
    && ls -lah target

EXPOSE 8080

CMD ["java", "-jar", "target/app.jar"]
