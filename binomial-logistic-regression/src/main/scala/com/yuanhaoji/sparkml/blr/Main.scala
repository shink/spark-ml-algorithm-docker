package com.yuanhaoji.sparkml.blr

import com.yuanhaoji.sparkml.common.parameter.CommonParser
import com.yuanhaoji.sparkml.common.util.PathUtil
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML Binomial Logistic Regression Job")
          .master(parameter.getMasterUrl)
          .getOrCreate()

        val trainDatasetPath = parameter.getTrainDatasetPath

        if (PathUtil.isHdfsPath(trainDatasetPath)) {
            val hadoopConf = spark.sparkContext.hadoopConfiguration
            hadoopConf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
            hadoopConf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
        }

        // Load training data
        val training = spark.read.format("libsvm").load(trainDatasetPath)

        val lr = new LogisticRegression()
          .setMaxIter(10)
          .setRegParam(0.3)
          .setElasticNetParam(0.8)

        // Fit the model
        val lrModel = lr.fit(training)

        // Print the coefficients and intercept for logistic regression
        println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

        // We can also use the multinomial family for binary classification
        val mlr = new LogisticRegression()
          .setMaxIter(10)
          .setRegParam(0.3)
          .setElasticNetParam(0.8)
          .setFamily("multinomial")

        val mlrModel = mlr.fit(training)

        // Print the coefficients and intercepts for logistic regression with multinomial family
        println(s"Multinomial coefficients: ${mlrModel.coefficientMatrix}")
        println(s"Multinomial intercepts: ${mlrModel.interceptVector}")

        spark.stop()
    }

}
