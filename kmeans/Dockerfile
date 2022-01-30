FROM openjdk:8-jdk-slim

LABEL maintainer="Yuanhao JI" \
    email="jiyuanhao1997@gmail.com" \
    url="https://github.com/shink"

RUN apt-get update && apt-get clean && rm -rf /tmp/* && rm -rf /var/lib/apt/lists/*

ENV JAVA_OPTS "-Xms128m -Xmx512m"
ARG version

WORKDIR /sparkml
COPY target/kmeans-${version}-jar-with-dependencies.jar kmeans.jar
COPY entrypoint.sh .
COPY dataset ./dataset

ENTRYPOINT ["./entrypoint.sh", "kmeans.jar"]
