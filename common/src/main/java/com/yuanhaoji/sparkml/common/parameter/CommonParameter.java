package com.yuanhaoji.sparkml.common.parameter;

import com.beust.jcommander.Parameter;

public class CommonParameter {

    @Parameter(names = {"-h", "--help"}, description = "Show help message", help = true)
    private boolean help;

    @Parameter(names = "--master", required = true,
            description = "The Spark master URL to connect to, " +
                    "such as \"local\" to run locally, \"local[4]\" to run locally with 4 cores, " +
                    "or \"spark://master:7077\" to run on a Spark standalone cluster")
    private String masterUrl;

    @Parameter(names = "--train-dataset", description = "The train dataset path")
    private String trainDatasetPath;

    @Parameter(names = "--test-dataset", description = "The test dataset path")
    private String testDatasetPath;

    @Parameter(names = "--validate-dataset", description = "The validate dataset path")
    private String validateDatasetPath;

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public String getTrainDatasetPath() {
        return trainDatasetPath;
    }

    public void setTrainDatasetPath(String trainDatasetPath) {
        this.trainDatasetPath = trainDatasetPath;
    }

    public String getTestDatasetPath() {
        return testDatasetPath;
    }

    public void setTestDatasetPath(String testDatasetPath) {
        this.testDatasetPath = testDatasetPath;
    }

    public String getValidateDatasetPath() {
        return validateDatasetPath;
    }

    public void setValidateDatasetPath(String validateDatasetPath) {
        this.validateDatasetPath = validateDatasetPath;
    }

    @Override
    public String toString() {
        return "CommonParameter{" +
                "help=" + help +
                ", masterUrl='" + masterUrl + '\'' +
                ", trainDatasetPath='" + trainDatasetPath + '\'' +
                ", testDatasetPath='" + testDatasetPath + '\'' +
                ", validateDatasetPath='" + validateDatasetPath + '\'' +
                '}';
    }
}
