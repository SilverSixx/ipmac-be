FROM maven:3.8.3-openjdk-17 AS package

WORKDIR /app
# Copy the parent POM
COPY ./pom.xml ./
# Copy all module POMs
COPY common-service/pom.xml ./common-service/
COPY admin-service/pom.xml ./admin-service/
COPY training-service/pom.xml ./training-service/

# Download all dependencies first (this layer can be cached)
RUN mvn dependency:go-offline -B

# Copy source code
COPY common-service/src ./common-service/src
COPY admin-service/src ./admin-service/src
COPY training-service/src ./training-service/src

# Build all modules
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine AS builder

# Create custom JRE
RUN $JAVA_HOME/bin/jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /opt/jlinked

# Common base for all services
FROM alpine:latest AS base
COPY --from=builder /opt/jlinked /opt/jlinked
ENV JAVA_HOME=/opt/jlinked
ENV PATH="$JAVA_HOME/bin:$PATH"
WORKDIR /app

# Common service
FROM base AS common-service
COPY --from=package /app/common-service/target/*.jar ./common-service/app.jar
CMD ["java", "-XX:+EnableDynamicAgentLoading", "-jar", "common-service/app.jar"]

# Admin service
FROM base AS admin-service
COPY --from=package /app/admin-service/target/*.jar ./admin-service/app.jar
CMD ["java", "-XX:+EnableDynamicAgentLoading", "-jar", "admin-service/app.jar"]

# Training service
FROM base AS training-service
COPY --from=package /app/training-service/target/*.jar ./training-service/app.jar
CMD ["java", "-XX:+EnableDynamicAgentLoading", "-jar", "training-service/app.jar"]