package com.hxbcc.commons.vfs2.provider;

import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;

/**
 * @author 凯少
 * @create 2022-09-14 15:41
 */
public class MinioSystemConfigBuilder extends FileSystemConfigBuilder {
    private static final MinioSystemConfigBuilder BUILDER = new MinioSystemConfigBuilder();

    @Override
    protected Class<? extends FileSystem> getConfigClass() {
        return MinioFileSystem.class;
    }

    public static MinioSystemConfigBuilder getInstance() {
        return BUILDER;
    }

    private MinioSystemConfigBuilder() {
        super("minio.");
    }
}
