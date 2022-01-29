package com.yuanhaoji.sparkml.common.util;

import java.util.Objects;

/**
 * @author shenke
 * @since 1.8
 */
public class PathUtil {

    public static final String HDFS_PATH_PREFIX = "hdfs://";

    public static boolean isHdfsPath(String path) {
        return path != null && path.startsWith(HDFS_PATH_PREFIX);
    }

    public static HdfsPath split(String hdfsPath) {
        int length = hdfsPath.length();
        int idx = HDFS_PATH_PREFIX.length();
        while (idx < length && hdfsPath.charAt(idx) != '/') {
            ++idx;
        }

        String uri = hdfsPath.substring(0, idx);
        String path = hdfsPath.substring(idx);
        return new HdfsPath(uri, path);
    }

    public static class HdfsPath {
        private final String uri;
        private final String path;

        public HdfsPath(String uri, String path) {
            this.uri = uri;
            this.path = path;
        }

        public String getUri() {
            return uri;
        }

        public String getPath() {
            return path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HdfsPath hdfsPath = (HdfsPath) o;
            return Objects.equals(uri, hdfsPath.uri) && Objects.equals(path, hdfsPath.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uri, path);
        }

        @Override
        public String toString() {
            return "HdfsPath{" +
                    "uri='" + uri + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }

}
