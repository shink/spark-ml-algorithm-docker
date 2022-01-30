package com.yuanhaoji.sparkml.gmm

import com.yuanhaoji.sparkml.common.parameter.CommonParser
import com.yuanhaoji.sparkml.common.util.PathUtil
import org.apache.spark.ml.clustering.GaussianMixture
import org.apache.spark.sql.SparkSession

object Main {

    def main(args: Array[String]): Unit = {
        val parser = new CommonParser()
        val parameter = parser.parse(args)

        val spark = SparkSession
          .builder
          .appName("Apache Spark ML GMM Job")
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

        // Trains Gaussian Mixture Model
        val gmm = new GaussianMixture()
          .setK(2)
        val model = gmm.fit(dataset)

        // output parameters of mixture model model
        for (i <- 0 until model.getK) {
            println(s"Gaussian $i:\nweight=${model.weights(i)}\n" +
              s"mu=${model.gaussians(i).mean}\nsigma=\n${model.gaussians(i).cov}\n")
        }

        spark.stop()
    }

}
