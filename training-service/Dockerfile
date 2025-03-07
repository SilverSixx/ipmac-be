FROM eclipse-temurin:17-jdk-alpine AS build

LABEL authors="dat_pl"

WORKDIR /opt

RUN $JAVA_HOME/bin/jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /opt/jlinked

FROM alpine:latest

COPY --from=build /opt/jlinked /opt/jlinked

ENV JAVA_HOME=/opt/jlinked
ENV PATH="$JAVA_HOME/bin:$PATH"

COPY target/*.jar app.jar

CMD ["java","-XX:+EnableDynamicAgentLoading", "-jar", "app.jar"]