FROM gradle:jdk11-alpine as base

WORKDIR /app

COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradlew
COPY gradle.properties ./gradle.properties
COPY input ./input
COPY src ./src

VOLUME ["./reports/tests:build/reports/tests"]

FROM base as test
RUN ["gradle", "assemble", "testClasses"]
