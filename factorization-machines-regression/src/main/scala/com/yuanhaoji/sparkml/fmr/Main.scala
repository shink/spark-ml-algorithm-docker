package com.yuanhaoji.sparkml.fmr

import com.yuanhaoji.sparkml.common.io.hadoop.HdfsFileManager
import com.yuanhaoji.sparkml.common.parameter.CommonParser
import com.yuanhaoji.sparkml.common.util.PathUtil
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.MinMaxScaler
import org.apache.spark.ml.regression.{FMRegressionModel, FMRegressor}
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML Factorization Machines Regression Job")
          .master(parameter.getMasterUrl)
          .getOrCreate()

        val trainDatasetPath = parameter.getTrainDatasetPath

        // Copy the directory from the local file system to hdfs file system.
        // For example, the parameter is hdfs://master:9000/dir/dataset,
        // then the directory at /dir/dataset will be copied
        // to the hdfs file system at hdfs://master:9000/dir/dataset.
        if (PathUtil.isHdfsPath(trainDatasetPath)) {
            val hdfsPath = PathUtil.split(trainDatasetPath)
            val uri = hdfsPath.getUri
            val path = hdfsPath.getPath

            val fileManager = new HdfsFileManager(uri, "root")
            fileManager.copy(path, path)

            val hadoopConf = spark.sparkContext.hadoopConfiguration
            hadoopConf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
            hadoopConf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
        }

        // Load and parse the data file, converting it to a DataFrame
        val data = spark.read.format("libsvm").load(trainDatasetPath)

        // Scale features
        val featureScaler = new MinMaxScaler()
          .setInputCol("features")
          .setOutputCol("scaledFeatures")
          .fit(data)

        // Split the data into training and test sets (30% held out for testing)
        val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

        // Train a FM model
        val fm = new FMRegressor()
          .setLabelCol("label")
          .setFeaturesCol("scaledFeatures")
          .setStepSize(0.001)

        // Create a Pipeline
        val pipeline = new Pipeline()
          .setStages(Array(featureScaler, fm))

        // Train model
        val model = pipeline.fit(trainingData)

        // Make predictions
        val predictions = model.transform(testData)

        // Select example rows to display
        predictions.select("prediction", "label", "features").show(5)

        // Select (prediction, true label) and compute test error
        val evaluator = new RegressionEvaluator()
          .setLabelCol("label")
          .setPredictionCol("prediction")
          .setMetricName("rmse")
        val rmse = evaluator.evaluate(predictions)
        println(s"Root Mean Squared Error (RMSE) on test data = $rmse")

        val fmModel = model.stages(1).asInstanceOf[FMRegressionModel]
        println(s"Factors: ${fmModel.factors} Linear: ${fmModel.linear} " +
          s"Intercept: ${fmModel.intercept}")

        spark.stop()
    }

}
