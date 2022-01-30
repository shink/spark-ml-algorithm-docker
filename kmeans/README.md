## KMeans

<p align="center">
    <img src="https://img.shields.io/docker/stars/tsund/spark-ml-kmeans.svg" />
    <img src="https://img.shields.io/docker/pulls/tsund/spark-ml-kmeans.svg" />
    <img src="https://img.shields.io/docker/image-size/tsund/spark-ml-kmeans" />
    <img src="https://img.shields.io/docker/v/tsund/spark-ml-kmeans" />
</p>

### Usage

```shell
docker run -e MASTER_URL=spark://master:7077 \\
           -e MASTER_URL=spark://master:7077 \\
           -e TRAIN_DATASET_PATH=hdfs://master:9000/train-data.txt \\
           -e TEST_DATASET_PATH=hdfs://master:9000/test-data.txt \\
           -e VALIDATE_DATASET_PATH=hdfs://master:9000/validate-data.txt \\
           tsund/spark-ml-kmeans:${TAG}
```

or declare environment variables in a [file](env-file.example)

```shell
docker run --env-file env-file.example \\
           tsund/spark-ml-kmeans:${TAG}
```

### References

[v3.1.2 ml-clustering](https://spark.apache.org/docs/3.1.2/ml-clustering.html#k-means)

[spark/examples/ml/KMeansExample.scala](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/ml/KMeansExample.scala)
