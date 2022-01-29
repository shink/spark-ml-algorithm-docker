package com.yuanhaoji.sparkml.common.parameter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * @author shenke
 * @since 1.8
 */
public class CommonParser {

    public CommonParameter parse(String[] args) {
        CommonParameter parameter = new CommonParameter();
        JCommander jc = JCommander.newBuilder()
                .programName("spark-ml-algorithm")
                .addObject(parameter)
                .build();

        try {
            jc.parse(args);
        } catch (ParameterException e) {
            jc.usage();
            System.exit(-1);
        }

        if (parameter.isHelp()) {
            jc.usage();
            System.exit(0);
        }

        return parameter;
    }

}
