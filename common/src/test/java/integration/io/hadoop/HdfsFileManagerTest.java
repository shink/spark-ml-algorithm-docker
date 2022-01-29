package integration.io.hadoop;

import com.yuanhaoji.sparkml.common.io.FileManager;
import com.yuanhaoji.sparkml.common.io.hadoop.HdfsFileManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class HdfsFileManagerTest {

    private static FileManager fileManager = null;

    private final String rootPath = "/sparkml/integration";
    private final String fileName = "integration.txt";
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
    public void test() {
        // mkdir
        boolean mkdirSuccess = fileManager.mkdir(rootPath);
        Assert.assertTrue(mkdirSuccess);

        // create a file
        boolean createSuccess = fileManager.create(filePath);
        Assert.assertTrue(createSuccess);

        // write content to the file
        boolean writeSuccess = fileManager.write(filePath, contentBytes, 0, contentBytes.length);
        Assert.assertTrue(writeSuccess);

        // read content from the file
        byte[] actuals = fileManager.read(filePath);
        Assert.assertArrayEquals(contentBytes, actuals);

        // copy the current class file to hdfs file system
        String srcFilePath = this.getClass().getResource("").getPath();
        boolean success = fileManager.copy(srcFilePath, rootPath);
        Assert.assertTrue(success);
    }
}
