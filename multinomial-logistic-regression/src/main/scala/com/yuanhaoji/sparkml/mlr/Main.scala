package com.yuanhaoji.sparkml.mlr

import com.yuanhaoji.sparkml.common.io.hadoop.HdfsFileManager
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
          .appName("Apache Spark ML Multinomial Logistic Regression Job")
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
        }

        // Load training data
        val training = spark
          .read
          .format("libsvm")
          .load(trainDatasetPath)

        val lr = new LogisticRegression()
          .setMaxIter(10)
          .setRegParam(0.3)
          .setElasticNetParam(0.8)

        // Fit the model
        val lrModel = lr.fit(training)

        // Print the coefficients and intercept for multinomial logistic regression
        println(s"Coefficients: \n${lrModel.coefficientMatrix}")
        println(s"Intercepts: \n${lrModel.interceptVector}")

        val trainingSummary = lrModel.summary

        // Obtain the objective per iteration
        val objectiveHistory = trainingSummary.objectiveHistory
        println("objectiveHistory:")
        objectiveHistory.foreach(println)

        // for multiclass, we can inspect metrics on a per-label basis
        println("False positive rate by label:")
        trainingSummary.falsePositiveRateByLabel.zipWithIndex.foreach { case (rate, label) =>
            println(s"label $label: $rate")
        }

        println("True positive rate by label:")
        trainingSummary.truePositiveRateByLabel.zipWithIndex.foreach { case (rate, label) =>
            println(s"label $label: $rate")
        }

        println("Precision by label:")
        trainingSummary.precisionByLabel.zipWithIndex.foreach { case (prec, label) =>
            println(s"label $label: $prec")
        }

        println("Recall by label:")
        trainingSummary.recallByLabel.zipWithIndex.foreach { case (rec, label) =>
            println(s"label $label: $rec")
        }

        println("F-measure by label:")
        trainingSummary.fMeasureByLabel.zipWithIndex.foreach { case (f, label) =>
            println(s"label $label: $f")
        }

        val accuracy = trainingSummary.accuracy
        val falsePositiveRate = trainingSummary.weightedFalsePositiveRate
        val truePositiveRate = trainingSummary.weightedTruePositiveRate
        val fMeasure = trainingSummary.weightedFMeasure
        val precision = trainingSummary.weightedPrecision
        val recall = trainingSummary.weightedRecall
        println(s"Accuracy: $accuracy\nFPR: $falsePositiveRate\nTPR: $truePositiveRate\n" +
          s"F-measure: $fMeasure\nPrecision: $precision\nRecall: $recall")

        spark.stop()
    }

}
