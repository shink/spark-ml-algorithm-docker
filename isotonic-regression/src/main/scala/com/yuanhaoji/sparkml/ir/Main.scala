package com.yuanhaoji.sparkml.ir

import com.yuanhaoji.sparkml.common.parameter.CommonParser
import com.yuanhaoji.sparkml.common.util.PathUtil
import org.apache.spark.ml.regression.IsotonicRegression
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML Isotonic Regression Job")
          .master(parameter.getMasterUrl)
          .getOrCreate()

        val trainDatasetPath = parameter.getTrainDatasetPath

        if (PathUtil.isHdfsPath(trainDatasetPath)) {
            val hadoopConf = spark.sparkContext.hadoopConfiguration
            hadoopConf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
            hadoopConf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
        }

        // Loads data
        val dataset = spark.read.format("libsvm").load(trainDatasetPath)

        // Trains an isotonic regression model
        val ir = new IsotonicRegression()
        val model = ir.fit(dataset)

        println(s"Boundaries in increasing order: ${model.boundaries}\n")
        println(s"Predictions associated with the boundaries: ${model.predictions}\n")

        // Makes predictions
        model.transform(dataset).show()

        spark.stop()
    }

}
