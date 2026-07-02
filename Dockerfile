# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/java-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
