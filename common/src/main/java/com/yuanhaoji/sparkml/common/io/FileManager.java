package com.yuanhaoji.sparkml.common.io;

/**
 * @author shenke
 * @since 1.8
 */
public interface FileManager {

    /**
     * Create a directory.
     *
     * @param path the directory path
     * @return whether the directory has been created successfully,
     * or the directory at the given path exists
     */
    boolean mkdir(String path);

    /**
     * Create a file.
     *
     * @param filePath the file path
     * @return whether the file has been created successfully, or already exists
     */
    boolean create(String filePath);

    /**
     * Read content to a byte array.
     *
     * @param filePath the file path
     * @return the byte array containing the file content
     */
    byte[] read(String filePath);

    /**
     * Write content to file system.
     *
     * @param filePath the file path
     * @param content  the byte array containing the content to be written
     * @param off      the offset of the content in the array
     * @param len      the length of the content in the array
     * @return whether the content has been written successfully
     */
    boolean write(String filePath, byte[] content, int off, int len);

    /**
     * Copy the file from the local file system to another.
     *
     * @param srcFilePath the source file path
     * @param dstFilePath the destination file path
     * @return whether the file has been copied successfully
     */
    boolean copy(String srcFilePath, String dstFilePath);

}
