# Spark ML Algorithms on Docker

<p align="center">
    <a href="https://github.com/shink/spark-ml-algorithm-docker/workflows"><img src="https://github.com/shink/spark-ml-algorithm-docker/workflows/Release/badge.svg" /></a>
    <a href="LICENSE"><img src="https://img.shields.io/github/license/shink/spark-ml-algorithm-docker.svg" /></a>
    <img src="https://img.shields.io/github/repo-size/shink/spark-ml-algorithm-docker.svg" />
    <img src="https://img.shields.io/badge/language-scala-C22D40.svg" />
</p>

<p align="center">
    <a href="docs/README_zh.md"><b>中文文档</b></a> •
    <a href="https://hub.docker.com/u/tsund"><b>Docker Hub</b></a> •
    <a href="https://github.com/shink?tab=packages"><b>GitHub Packages</b></a>
</p>

## Algorithms

- [KMeans](kmeans)

## Development

Requirements:

- Java JDK 8+
- Maven 3.6.1+
- Docker 19.03.1+
- Hadoop 2+
- Spark 3+

Compile and build:

```shell
mvn clean package -DskipTests
```

Build docker image:

```shell
mvn clean package -DskipTests -Pdocker
```

## Reference

[v3.1.2 ml-guide](https://spark.apache.org/docs/3.1.2/ml-guide.html)

[examples on GitHub](https://github.com/apache/spark/tree/master/examples/src/main/scala/org/apache/spark/examples/ml)

## License

[MIT](LICENSE)
