package com.yuanhaoji.sparkml.common.io.hadoop;

import com.yuanhaoji.sparkml.common.io.FileManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author shenke
 * @since 1.8
 */
public class HdfsFileManager implements FileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsFileManager.class);

    private final URI uri;
    private final String user;

    /**
     * @param uri  the hdfs URI, such as, hdfs://master:9000
     * @param user the hdfs username
     * @throws URISyntaxException If the given string violates RFC 2396, as augmented
     *                            by the above deviations
     */
    public HdfsFileManager(String uri, String user) throws URISyntaxException {
        this.uri = new URI(uri);
        this.user = user;
    }

    @Override
    public boolean mkdir(String dirPath) {
        Configuration conf = new Configuration();
        FileSystem fs = null;

        try {
            fs = FileSystem.get(uri, conf, user);
            Path path = new Path(dirPath);

            if (fs.exists(path)) {
                LOGGER.warn("The file: {} already exists", dirPath);
            } else {
                fs.mkdirs(path);
            }
            return true;

        } catch (IOException | InterruptedException e) {
            LOGGER.error("Failed to create file: {}", dirPath);
            return false;

        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close hdfs file system");
                }
            }
        }
    }

    @Override
    public boolean create(String filePath) {
        Configuration conf = new Configuration();
        FileSystem fs = null;

        try {
            fs = FileSystem.get(uri, conf, user);
            Path path = new Path(filePath);

            if (fs.exists(path)) {
                LOGGER.warn("The file: {} already exists", filePath);
            } else {
                FSDataOutputStream os = fs.create(path);
                os.close();
                fs.close();
            }
            return true;

        } catch (IOException | InterruptedException e) {
            LOGGER.error("Failed to create file: {}", filePath);
            return false;

        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close hdfs file system");
                }
            }
        }
    }

    @Override
    public byte[] read(String filePath) {
        Configuration conf = new Configuration();
        FileSystem fs = null;

        try {
            fs = FileSystem.get(uri, conf, user);
            Path path = new Path(filePath);

            if (!fs.exists(path)) {
                LOGGER.warn("The file: {} doesn't exist", filePath);
                return null;
            }

            FSDataInputStream is = fs.open(path);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1 << 10];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            is.close();
            fs.close();
            return result.toByteArray();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Failed to read file from {}", filePath);
            return null;
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close hdfs file system");
                }
            }
        }
    }

    @Override
    public boolean write(String filePath, byte[] content, int off, int len) {
        Configuration conf = new Configuration();
        FileSystem fs = null;

        try {
            fs = FileSystem.get(uri, conf, user);
            Path path = new Path(filePath);
            if (fs.exists(path)) {
                LOGGER.warn("The file: {} already exists, will be rewritten", filePath);
            }
            FSDataOutputStream os = fs.create(path);
            os.write(content, off, len);
            os.flush();
            return true;

        } catch (IOException | InterruptedException e) {
            LOGGER.error("Failed to write to {}", filePath);
            return false;

        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close hdfs file system");
                }
            }
        }
    }

    @Override
    public boolean copy(String srcFilePath, String dstFilePath) {
        Configuration conf = new Configuration();
        FileSystem fs = null;
        try {
            fs = FileSystem.get(uri, conf, user);
            fs.copyFromLocalFile(new Path(srcFilePath), new Path(dstFilePath));
            return true;

        } catch (IOException | InterruptedException e) {
            LOGGER.error("Failed to copy file from {} to {}", srcFilePath, dstFilePath);
            return false;

        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to close hdfs file system");
                }
            }
        }
    }
}
