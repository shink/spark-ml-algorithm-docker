## Random Forest Classification

<p align="center">
    <img src="https://img.shields.io/docker/stars/tsund/spark-ml-random-forest-classification.svg" />
    <img src="https://img.shields.io/docker/pulls/tsund/spark-ml-random-forest-classification.svg" />
    <img src="https://img.shields.io/docker/image-size/tsund/spark-ml-random-forest-classification" />
    <img src="https://img.shields.io/docker/v/tsund/spark-ml-random-forest-classification" />
</p>

### Usage

```shell
docker run -e MASTER_URL=spark://master:7077 \\
           -e MASTER_URL=spark://master:7077 \\
           -e TRAIN_DATASET_PATH=hdfs://master:9000/train-data.txt \\
           -e TEST_DATASET_PATH=hdfs://master:9000/test-data.txt \\
           -e VALIDATE_DATASET_PATH=hdfs://master:9000/validate-data.txt \\
           tsund/spark-ml-random-forest-classification:${TAG}
```

or declare environment variables in a [file](env-file.example)

```shell
docker run --env-file env-file.example \\
           tsund/spark-ml-random-forest-classification:${TAG}
```

### References

[v3.1.2 ml-classification-regression](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#random-forest-classifier)

[spark/examples/ml/RandomForestClassifierExample.scala](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/ml/RandomForestClassifierExample.scala)
