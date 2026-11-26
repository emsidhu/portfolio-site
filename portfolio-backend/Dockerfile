# Stage 1: Compile the native binary using GraalVM 25
FROM ghcr.io/graalvm/native-image-community:25 AS build
WORKDIR /app

# Copy the Maven Wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Ensure mvnw is executable inside Linux (important since you are on Windows)
RUN chmod +x mvnw

COPY src ./src
RUN ./mvnw -Pnative native:compile -DskipTests

# Stage 2: Minimal Linux runtime (no JVM needed)
FROM debian:bookworm-slim
WORKDIR /app

COPY --from=build /app/target/portfolio-backend /app/portfolio-backend

EXPOSE 8080
ENTRYPOINT ["/app/portfolio-backend"]
