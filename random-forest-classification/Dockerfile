FROM openjdk:8-jdk-slim

LABEL maintainer="Yuanhao JI" \
    email="jiyuanhao1997@gmail.com" \
    url="https://github.com/shink"

RUN apt-get update && apt-get clean && rm -rf /tmp/* && rm -rf /var/lib/apt/lists/*

ENV JAVA_OPTS "-Xms128m -Xmx512m"
ARG JAR_FILE

WORKDIR /sparkml
COPY ${JAR_FILE} rfc.jar
COPY entrypoint.sh .

ENTRYPOINT ["sh", "./entrypoint.sh", "rfc.jar"]
