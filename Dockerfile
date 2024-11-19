FROM gradle:jdk21 as BUILD
WORKDIR /build
COPY --chown=gradle:gradle src /build/src
COPY --chown=gradle:gradle build.gradle settings.gradle /build/
RUN gradle --no-daemon shadowJar

FROM openjdk:21-slim
WORKDIR /app
COPY --from=BUILD /build/build/libs/rsskt.jar rsskt.jar
WORKDIR /app/work
ENTRYPOINT java -jar /app/rsskt.jar