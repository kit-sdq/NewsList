FROM gradle:7.2-jdk11 as builder
WORKDIR /usr/src/newslist
COPY . .
RUN chmod +x ./gradlew && ./gradlew build

FROM azul/zulu-openjdk-alpine:11
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /usr/src/newslist
COPY --from=builder /usr/src/newslist/build/libs/*SNAPSHOT.jar news.jar

EXPOSE 8080
ENTRYPOINT java -jar news.jar