package com.yuanhaoji.sparkml.nb

import com.yuanhaoji.sparkml.common.parameter.CommonParser
import com.yuanhaoji.sparkml.common.util.PathUtil
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML Naive Bayes Job")
          .master(parameter.getMasterUrl)
          .getOrCreate()

        val trainDatasetPath = parameter.getTrainDatasetPath

        if (PathUtil.isHdfsPath(trainDatasetPath)) {
            val hadoopConf = spark.sparkContext.hadoopConfiguration
            hadoopConf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
            hadoopConf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
        }

        // Load the data stored in LIBSVM format as a DataFrame
        val data = spark.read.format("libsvm").load(trainDatasetPath)

        // Split the data into training and test sets (30% held out for testing)
        val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3), seed = 1234L)

        // Train a NaiveBayes model
        val model = new NaiveBayes()
          .fit(trainingData)

        // Select example rows to display
        val predictions = model.transform(testData)
        predictions.show()

        // Select (prediction, true label) and compute test error
        val evaluator = new MulticlassClassificationEvaluator()
          .setLabelCol("label")
          .setPredictionCol("prediction")
          .setMetricName("accuracy")
        val accuracy = evaluator.evaluate(predictions)
        println(s"Test set accuracy = $accuracy")

        spark.stop()
    }

}
