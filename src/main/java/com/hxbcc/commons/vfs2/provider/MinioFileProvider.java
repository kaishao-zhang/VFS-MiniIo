package com.hxbcc.commons.vfs2.provider;

import io.minio.MinioClient;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs2.provider.GenericFileName;
import org.apache.commons.vfs2.provider.GenericURLFileName;
import org.apache.commons.vfs2.provider.http.HttpFileNameParser;
import org.apache.commons.vfs2.provider.http.HttpFileSystemConfigBuilder;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author 凯少
 * @create 2022-09-08 17:04
 */
public class MinioFileProvider extends AbstractOriginatingFileProvider {

    public static final UserAuthenticationData.Type[] AUTHENTICATOR_TYPES = new UserAuthenticationData.Type[] {
            UserAuthenticationData.USERNAME, UserAuthenticationData.PASSWORD };

    static final Collection<Capability> CAPABILITIES = Collections
            .unmodifiableCollection(Arrays.asList(Capability.GET_TYPE, Capability.READ_CONTENT,
                    Capability.CREATE,
                    Capability.DELETE,
                    Capability.RENAME,
                    Capability.WRITE_CONTENT,
                    Capability.URI, Capability.GET_LAST_MODIFIED,
                    Capability.SET_LAST_MODIFIED_FILE,
                    Capability.ATTRIBUTES, Capability.RANDOM_ACCESS_READ, Capability.DIRECTORY_READ_CONTENT,
                    Capability.LIST_CHILDREN));

    @Override
    protected FileSystem doCreateFileSystem(FileName name, FileSystemOptions fileSystemOptions) throws FileSystemException {
        // Create the file system
        final GenericFileName rootName = (GenericFileName) name;

        UserAuthenticationData authData = null;
        MinioClient minioClient;
        try {
            authData = UserAuthenticatorUtils.authenticate(fileSystemOptions, AUTHENTICATOR_TYPES);

            final String fileScheme = rootName.getScheme();
            final char lastChar = fileScheme.charAt(fileScheme.length() - 1);
            final String internalScheme = (lastChar == 's' || lastChar == 'S') ? "https" : "http";

            minioClient = MinioClientFactory.createConnection(internalScheme, rootName.getHostName(),
                    rootName.getPort(),
                    UserAuthenticatorUtils.toString(UserAuthenticatorUtils.getData(authData,
                            UserAuthenticationData.USERNAME, UserAuthenticatorUtils.toChar(rootName.getUserName()))),
                    UserAuthenticatorUtils.toString(UserAuthenticatorUtils.getData(authData,
                            UserAuthenticationData.PASSWORD, UserAuthenticatorUtils.toChar(rootName.getPassword()))),
                    fileSystemOptions);
        } finally {
            UserAuthenticatorUtils.cleanup(authData);
        }

        return new MinioFileSystem(rootName, minioClient, fileSystemOptions);
    }

    @Override
    public Collection<Capability> getCapabilities() {
        return CAPABILITIES;
    }

    @Override
    public FileSystemConfigBuilder getConfigBuilder() {
        return HttpFileSystemConfigBuilder.getInstance();
    }
    /**
     * Constructs a new HdfsFileProvider.
     */
    public MinioFileProvider() {
        this.setFileNameParser(HttpFileNameParser.getInstance());
    }


    @Override
    public FileObject findFile(FileObject baseFileObject, String uri, FileSystemOptions fileSystemOptions) throws FileSystemException {
        // Parse the URI
        final FileName name;
        try {
            name = parseUri(baseFileObject != null ? baseFileObject.getName() : null, uri);
        } catch (final FileSystemException exc) {
            throw new FileSystemException("vfs.provider/invalid-absolute-uri.error", uri, exc);
        }

        // Locate the file
        return findFile(name, fileSystemOptions);
    }

    /**
     * 重写该方法，设置Minio的路径，格式为：/桶/
     */
    protected FileObject findFile(final FileName fileName, final FileSystemOptions fileSystemOptions)
            throws FileSystemException {
        // Check in the cache for the file system
        final FileName rootName = getContext().getFileSystemManager().resolveName(fileName,"/test-bucket/");

        final FileSystem fs = getFileSystem(rootName, fileSystemOptions);

        // Locate the file
        // return fs.resolveFile(name.getPath());
        return fs.resolveFile(fileName);
    }
}
