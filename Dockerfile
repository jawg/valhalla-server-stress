FROM openjdk:8-jdk-slim AS builder

WORKDIR /opt/gatling

COPY build.gradle.kts gradlew settings.gradle.kts gradle.properties ./
COPY src src
COPY gradle gradle

RUN ./gradlew shadowJar

FROM openjdk:8-jre-slim

WORKDIR /opt/gatling

ENV GATLING_JAR=/opt/gatling/valhalla-server-stress.jar
ENV GATLING_PROPERTIES=
ENV SERVER_URL=
ENV REGIONS_FILE=
ENV SEEDS_FILE=
ENV USERS_COUNT=
ENV USERS_RAMP_TIME=
ENV GENERATE_SEEDS=
ENV AUTO_GENERATE_SEEDS=
ENV PROFILE=

COPY --from=builder /opt/gatling/build/libs/valhalla-server-stress-all.jar $GATLING_JAR
COPY bin/valhalla-server-stress /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
