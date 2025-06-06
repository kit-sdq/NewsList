FROM gradle:8.14-jdk17 as builder
WORKDIR /usr/src/newslist
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:21

USER 999

WORKDIR /usr/src/newslist
COPY --from=builder /usr/src/newslist/build/libs/*.jar news.jar

EXPOSE 8080
ENTRYPOINT java -jar news.jar
