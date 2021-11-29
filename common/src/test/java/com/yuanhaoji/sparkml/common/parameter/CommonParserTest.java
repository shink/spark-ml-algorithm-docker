package com.yuanhaoji.sparkml.common.parameter;

import org.junit.Assert;
import org.junit.Test;

public class CommonParserTest {

    @Test
    public void parse() {
        String masterUrl = "spark://master:7077";
        String trainDatasetPath = "hdfs://master:9000/dataset/kmeans/sample_kmeans_data.txt";
        String[] args = new String[]{"--master", masterUrl, "--train-dataset", trainDatasetPath};

        CommonParser parser = new CommonParser();
        CommonParameter parameter = parser.parse(args);

        Assert.assertEquals(masterUrl, parameter.getMasterUrl());
        Assert.assertEquals(trainDatasetPath, parameter.getTrainDatasetPath());
        Assert.assertNull(parameter.getTestDatasetPath());
        Assert.assertNull(parameter.getValidateDatasetPath());
    }
}
