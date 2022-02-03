package com.yuanhaoji.sparkml.lr

import com.yuanhaoji.sparkml.common.io.hadoop.HdfsFileManager
import com.yuanhaoji.sparkml.common.parameter.CommonParser
import com.yuanhaoji.sparkml.common.util.PathUtil
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML Linear Regression Job")
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

        // Load training data
        val training = spark.read.format("libsvm").load(trainDatasetPath)

        val lr = new LinearRegression()
          .setMaxIter(10)
          .setRegParam(0.3)
          .setElasticNetParam(0.8)

        // Fit the model
        val lrModel = lr.fit(training)

        // Print the coefficients and intercept for linear regression
        println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

        // Summarize the model over the training set and print out some metrics
        val trainingSummary = lrModel.summary
        println(s"numIterations: ${trainingSummary.totalIterations}")
        println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
        trainingSummary.residuals.show()
        println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
        println(s"r2: ${trainingSummary.r2}")

        spark.stop()
    }

}
