FROM openjdk:17-alpine

ARG JAR_FILE=./target/itmo-db-coursework.jar

COPY ${JAR_FILE} .

ENTRYPOINT ["java", "-jar", "/itmo-db-coursework.jar"]