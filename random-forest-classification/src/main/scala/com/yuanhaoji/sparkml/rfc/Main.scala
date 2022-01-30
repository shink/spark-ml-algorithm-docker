package com.yuanhaoji.sparkml.rfc

import com.yuanhaoji.sparkml.common.io.hadoop.HdfsFileManager
import com.yuanhaoji.sparkml.common.parameter.CommonParser
import com.yuanhaoji.sparkml.common.util.PathUtil
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{RandomForestClassificationModel, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML Random Forest Classification Job")
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

        // Load and parse the data file, converting it to a DataFrame.
        val data = spark.read.format("libsvm").load(trainDatasetPath)

        // Index labels, adding metadata to the label column.
        // Fit on whole dataset to include all labels in index.
        val labelIndexer = new StringIndexer()
          .setInputCol("label")
          .setOutputCol("indexedLabel")
          .fit(data)

        // Automatically identify categorical features, and index them.
        // Set maxCategories so features with > 4 distinct values are treated as continuous.
        val featureIndexer = new VectorIndexer()
          .setInputCol("features")
          .setOutputCol("indexedFeatures")
          .setMaxCategories(4)
          .fit(data)

        // Split the data into training and test sets (30% held out for testing).
        val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

        // Train a RandomForest model.
        val rf = new RandomForestClassifier()
          .setLabelCol("indexedLabel")
          .setFeaturesCol("indexedFeatures")
          .setNumTrees(10)

        // Convert indexed labels back to original labels.
        val labelConverter = new IndexToString()
          .setInputCol("prediction")
          .setOutputCol("predictedLabel")
          .setLabels(labelIndexer.labelsArray(0))

        // Chain indexers and forest in a Pipeline.
        val pipeline = new Pipeline()
          .setStages(Array(labelIndexer, featureIndexer, rf, labelConverter))

        // Train model. This also runs the indexers.
        val model = pipeline.fit(trainingData)

        // Make predictions.
        val predictions = model.transform(testData)

        // Select example rows to display.
        predictions.select("predictedLabel", "label", "features").show(5)

        // Select (prediction, true label) and compute test error.
        val evaluator = new MulticlassClassificationEvaluator()
          .setLabelCol("indexedLabel")
          .setPredictionCol("prediction")
          .setMetricName("accuracy")
        val accuracy = evaluator.evaluate(predictions)
        println(s"Test Error = ${(1.0 - accuracy)}")

        val rfModel = model.stages(2).asInstanceOf[RandomForestClassificationModel]
        println(s"Learned classification forest model:\n ${rfModel.toDebugString}")

        spark.stop()
    }

}
