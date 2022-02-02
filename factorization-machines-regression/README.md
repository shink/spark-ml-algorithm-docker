## Factorization Machines Regression

<p align="center">
    <img src="https://img.shields.io/docker/stars/tsund/spark-ml-factorization-machines-regression.svg" />
    <img src="https://img.shields.io/docker/pulls/tsund/spark-ml-factorization-machines-regression.svg" />
    <img src="https://img.shields.io/docker/image-size/tsund/spark-ml-factorization-machines-regression" />
    <img src="https://img.shields.io/docker/v/tsund/spark-ml-factorization-machines-regression" />
</p>

### Usage

```shell
docker run -e MASTER_URL=spark://master:7077 \\
           -e MASTER_URL=spark://master:7077 \\
           -e TRAIN_DATASET_PATH=hdfs://master:9000/train-data.txt \\
           -e TEST_DATASET_PATH=hdfs://master:9000/test-data.txt \\
           -e VALIDATE_DATASET_PATH=hdfs://master:9000/validate-data.txt \\
           tsund/spark-ml-factorization-machines-regression:${TAG}
```

or declare environment variables in a [file](env-file.example)

```shell
docker run --env-file env-file.example \\
           tsund/spark-ml-factorization-machines-regression:${TAG}
```

### References

[v3.1.2 ml-classification-regression](https://spark.apache.org/docs/3.1.2/ml-classification-regression.html#factorization-machines-regressor)

[spark/examples/ml/FMRegressorExample.scala](https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/ml/FMRegressorExample.scala)
