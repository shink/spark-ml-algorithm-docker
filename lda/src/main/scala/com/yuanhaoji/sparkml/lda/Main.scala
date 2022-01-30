package com.yuanhaoji.sparkml.lda

import com.yuanhaoji.sparkml.common.parameter.CommonParser
import com.yuanhaoji.sparkml.common.util.PathUtil
import org.apache.spark.ml.clustering.LDA
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML LDA Job")
          .master(parameter.getMasterUrl)
          .getOrCreate()

        val trainDatasetPath = parameter.getTrainDatasetPath

        if (PathUtil.isHdfsPath(trainDatasetPath)) {
            val hadoopConf = spark.sparkContext.hadoopConfiguration
            hadoopConf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
            hadoopConf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
        }

        // Loads data
        val dataset = spark.read.format("libsvm")
          .load(trainDatasetPath)

        // Trains a LDA model
        val lda = new LDA().setK(10).setMaxIter(10)
        val model = lda.fit(dataset)

        val ll = model.logLikelihood(dataset)
        val lp = model.logPerplexity(dataset)
        println(s"The lower bound on the log likelihood of the entire corpus: $ll")
        println(s"The upper bound on perplexity: $lp")

        // Describe topics
        val topics = model.describeTopics(3)
        println("The topics described by their top-weighted terms:")
        topics.show(false)

        // Shows the result
        val transformed = model.transform(dataset)
        transformed.show(false)

        spark.stop()
    }

}
