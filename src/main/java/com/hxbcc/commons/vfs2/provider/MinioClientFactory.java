package com.hxbcc.commons.vfs2.provider;

import io.minio.MinioClient;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.http.HttpFileSystemConfigBuilder;

/**
 * @author 凯少
 * @create 2022-09-14 11:08
 */
public class MinioClientFactory {

    public static MinioClient createConnection(final MinioSystemConfigBuilder builder,
                                               final String scheme, final String hostname,
                                               final int port, final String username,
                                               final String password,
                                               final FileSystemOptions fileSystemOptions) {
        //todo 设置传入的builder参数
        final MinioClient client;
        String endpoint = "http://" + hostname + ":" + port;
        client = MinioClient.builder().endpoint(endpoint).credentials(username, password).build();
        return client;
    }

    public static MinioClient createConnection(final String scheme, final String hostname, final int port, final String username, final String password, final FileSystemOptions fileSystemOptions) throws FileSystemException {
        return createConnection(MinioSystemConfigBuilder.getInstance(), scheme, hostname, port, username, password, fileSystemOptions);
    }

    private MinioClientFactory() {
    }
}
