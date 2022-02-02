# Spark ML Algorithms on Docker

<p align="center">
    <a href="https://github.com/shink/spark-ml-algorithm-docker/actions/workflows/release.yml"><img src="https://github.com/shink/spark-ml-algorithm-docker/workflows/Release/badge.svg" /></a>
    <a href="../LICENSE"><img src="https://img.shields.io/github/license/shink/spark-ml-algorithm-docker.svg" /></a>
    <img src="https://img.shields.io/badge/language-scala-C22D40.svg" />
    <img src="https://img.shields.io/github/v/release/shink/spark-ml-algorithm-docker" />
</p>

<p align="center">
    <a href="../README.md"><b>English Document</b></a> •
    <a href="https://hub.docker.com/u/tsund"><b>Docker Hub</b></a> •
    <a href="https://github.com/shink?tab=packages"><b>GitHub Packages</b></a> •
</p>

## 算法

- [KMeans](../kmeans)
- [Latent Dirichlet Allocation](../lda)
- [Gaussian Mixture Model](../gmm)
- [Binomial Logistic Regression](../binomial-logistic-regression)
- [Multinomial Logistic Regression](../multinomial-logistic-regression)
- [Decision Tree Classification](../decision-tree-classification)
- [Random Forest Classification](../random-forest-classification)
- [Gradient-boosted Tree Classification](../gradient-boosted-tree-classification)
- [Isotonic Regression](../isotonic-regression)
- [Factorization Machines Regression](../factorization-machines-regression)

## 开发

环境需求:

- JDK 8+
- Maven 3+
- Docker 19+
- Hadoop 2+
- Spark 3+

编译：

```shell
mvn clean package -DskipTests
```

构建 Docker 镜像：

```shell
mvn clean package -DskipTests -Pdocker
```

## 参考

[v3.1.2 ml-guide](https://spark.apache.org/docs/3.1.2/ml-guide.html)

[examples on GitHub](https://github.com/apache/spark/tree/master/examples/src/main/scala/org/apache/spark/examples/ml)

## 许可证

[MIT](LICENSE)
