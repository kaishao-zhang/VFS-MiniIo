package com.hxbcc.commons.vfs2.provider;

import io.minio.MinioClient;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;
import org.apache.commons.vfs2.provider.GenericFileName;
import org.apache.hadoop.fs.Path;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;

/**
 * @author 凯少
 * @create 2022-09-08 17:21
 */
public class MinioFileSystem extends AbstractFileSystem {

    private final MinioClient client;

    protected MinioFileSystem(final GenericFileName rootName, final MinioClient client,
                              final FileSystemOptions fileSystemOptions) {
        super(rootName, null, fileSystemOptions);
        this.client = client;
    }

    protected MinioClient getClient() {
        return client;
    }

    @Override
    protected FileObject createFile(AbstractFileName name) throws Exception {
        return this.resolveFile(name);
//        Item item = getClientByName(name);
//        return new MinioFileObject<>(name, this, item);
    }


    @Override
    protected void addCapabilities(Collection<Capability> caps) {
        caps.addAll(MinioFileProvider.CAPABILITIES);
    }


    @Override
    public FileObject resolveFile(FileName name) throws FileSystemException {
        final boolean useCache = null != getFileSystemManager().getFilesCache();
        FileObject fileObject = useCache ? getFileFromCache(name) : null;
        if (null == fileObject) {
            String path;
            try {
                path = URLDecoder.decode(name.getPath(), "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                path = name.getPath();
            }
            final Path filePath = new Path(path);
            try {
                fileObject = new MinioFileObject<>((AbstractFileName) name, this, client, filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            fileObject = new HdfsFileObject((AbstractFileName) name, this, fs, filePath);
            fileObject = decorateFileObject(fileObject);
            if (useCache) {
                this.putFileToCache(fileObject);
            }
        }

        if (getFileSystemManager().getCacheStrategy().equals(CacheStrategy.ON_RESOLVE)) {
            fileObject.refresh();
        }
        return fileObject;
    }

}
