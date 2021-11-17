FROM gradle:7.3.0-jdk11 as builder
WORKDIR /usr/src/newslist
COPY . .
RUN chmod +x ./gradlew && ./gradlew build

FROM azul/zulu-openjdk-alpine:11

USER 999

WORKDIR /usr/src/newslist
COPY --from=builder /usr/src/newslist/build/libs/*SNAPSHOT.jar news.jar

EXPOSE 8080
ENTRYPOINT java -jar news.jar
