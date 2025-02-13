FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn
COPY src src
COPY data ./data 

RUN chmod +x mvnw && ./mvnw clean package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jre-noble AS runtime

WORKDIR /app

COPY --from=builder /src/target/movies-0.0.1-SNAPSHOT.jar app.jar
COPY data app/data  

RUN apt update && apt install -y curl

# Set environment variables
ENV PORT=8080
ENV SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/movies
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=
ENV SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/movies
ENV SPRING_DATA_REDIS_HOST=localhost
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_DATABASE=0

# Expose the application port
EXPOSE ${PORT}

# Health check for container
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f -s http://localhost:${PORT}/health || exit 1


ENTRYPOINT ["java", "-jar", "/app/app.jar"]
