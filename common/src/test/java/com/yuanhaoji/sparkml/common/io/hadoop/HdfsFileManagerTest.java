package com.yuanhaoji.sparkml.common.io.hadoop;

import com.yuanhaoji.sparkml.common.io.FileManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class HdfsFileManagerTest {

    private static FileManager fileManager = null;

    private final String rootPath = "/sparkml/ut";
    private final String fileName = "ut.txt";
    private final String filePath = rootPath + File.separator + fileName;

    private final String content = "Hello, Hadoop!";
    private final byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

    @BeforeClass
    public static void setup() {
        String uri = "hdfs://192.168.144.20:9000";
        String user = "shenke";
        try {
            fileManager = new HdfsFileManager(uri, user);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            Assert.assertNotNull(fileManager);
        }
    }

    @Test
    public void mkdir() {
        boolean success = fileManager.mkdir(rootPath);
        Assert.assertTrue(success);
    }

    @Test
    public void create() {
        boolean success = fileManager.create(filePath);
        Assert.assertTrue(success);
    }

    @Test
    public void write() {
        boolean success = fileManager.write(filePath, contentBytes, 0, contentBytes.length);
        Assert.assertTrue(success);
    }

    @Test
    public void copy() {
        String srcFilePath = this.getClass().getResource("").getPath();
        boolean success = fileManager.copy(srcFilePath, rootPath);
        Assert.assertTrue(success);
    }
}
