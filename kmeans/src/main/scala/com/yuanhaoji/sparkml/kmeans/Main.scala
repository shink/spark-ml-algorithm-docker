package com.yuanhaoji.sparkml.kmeans

import com.yuanhaoji.sparkml.common.parameter.CommonParser
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML KMeans Job")
          .master(parameter.getMasterUrl)
          .getOrCreate()

        // Loads data
        val dataset = spark.read.format("libsvm").load(parameter.getTrainDatasetPath)

        // Trains a k-means model
        val kmeans = new KMeans().setK(2).setSeed(1L)
        val model = kmeans.fit(dataset)

        // Make predictions
        val predictions = model.transform(dataset)

        // Evaluate clustering by computing Silhouette score
        val evaluator = new ClusteringEvaluator()

        val silhouette = evaluator.evaluate(predictions)
        println(s"Silhouette with squared euclidean distance = $silhouette")

        // Shows the result
        println("Cluster Centers: ")
        model.clusterCenters.foreach(println)

        spark.stop()
    }

}
