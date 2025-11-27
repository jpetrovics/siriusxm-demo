# Stage 1: build Spring Boot jar
FROM gradle:8.4-jdk17 AS builder
WORKDIR /src
COPY . .
RUN chmod +x ./gradlew && ./gradlew --no-daemon clean bootJar -x test
RUN ./gradlew --no-daemon clean bootJar -x test

# Stage 2: create custom JRE with jlink
FROM eclipse-temurin:17-jdk-alpine AS jlink
WORKDIR /work
COPY --from=builder /src/build/libs/*.jar app.jar
RUN mkdir unpack && cd unpack && jar -xf ../app.jar
RUN jlink --add-modules java.base,java.logging,java.management,java.desktop,java.instrument,java.net.http,java.prefs,java.rmi,java.scripting,java.security.jgss,java.sql.rowset,jdk.jfr,jdk.net,jdk.unsupported \
    --output /customjre --no-header-files --no-man-pages --compress=2

# Stage 3: smallest runtime on Alpine
FROM alpine:3.19
WORKDIR /app
# add libc compatibility for JVM
RUN apk add --no-cache libstdc++ zlib
COPY --from=builder /src/build/libs/*.jar app.jar
COPY --from=jlink /customjre /opt/jre
ENV PATH="/opt/jre/bin:$PATH"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
