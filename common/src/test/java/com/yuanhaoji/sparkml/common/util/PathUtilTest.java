package com.yuanhaoji.sparkml.common.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathUtilTest {

    @Test
    public void split() {
        String uri = "hdfs://master:7000";
        String path = "/sparkml/dir";
        PathUtil.HdfsPath expected = new PathUtil.HdfsPath(uri, path);

        PathUtil.HdfsPath actual = PathUtil.split(uri + path);

        Assert.assertEquals(expected, actual);
    }
}
