package com.hxbcc.commons.vfs2.provider;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileObject;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 凯少
 * @create 2022-09-08 17:18
 */
public class MinioFileObject<FS extends MinioFileSystem> extends AbstractFileObject<MinioFileSystem> {


    private final Item item;
    private final MinioClient client;
    private final Path path;
    private FileType type;
    private FileStatus stat;


    protected MinioFileObject(final AbstractFileName name, final FS fileSystem, final MinioClient client, final Path path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        this(name, fileSystem, MinioSystemConfigBuilder.getInstance(), client, path);
    }

    protected MinioFileObject(final AbstractFileName fileName, final FS fileSystem, final MinioSystemConfigBuilder builder, final MinioClient client, final Path path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        super(fileName, fileSystem);
        this.client = client;
        this.path = path;
        this.item = getClientByName(fileName);
        if (item == null) {
            this.type = FileType.IMAGINARY;
        }
        final FileSystemOptions fileSystemOptions = fileSystem.getFileSystemOptions();
    }

    private Item getClientByName(AbstractFileName name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String buket = "";
        String currentPath = "";
        //判断是否是根路径
        if (!"/".equals(name)) {
            buket = extractBuket(name.getPath());
            currentPath = extractParentFilePath(name.getPath(), buket);
        } else {
            buket = extractBuket(path.getName());
            currentPath = extractFilePath(path.getParent().toString());
        }
        return doGetItem(buket, currentPath);
    }

    private String extractParentFilePath(String pathTemp, String buket) {
        String parentFilePath = "";
        if (0 != pathTemp.lastIndexOf("/")) {
            int length = buket.length();
            parentFilePath = pathTemp.substring(length + 1);
        } else {
            parentFilePath = pathTemp.substring(1);
        }
        return parentFilePath;
    }

    private Item doGetItem(String buket, String pathTemp) {
        Iterable<Result<Item>> results = client.listObjects(ListObjectsArgs.builder().bucket(buket).prefix(pathTemp).build());
        List<Item> items = new ArrayList();
        Item item = null;
        if (results.iterator().hasNext()) {
            Result<Item> result = results.iterator().next();
            try {
                item = result.get();
                items.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                Item itemTemp = items.get(i);
                FileType type = null;
                try {
                    type = getType();
                    if (FileType.FOLDER == type) {
                        if (itemTemp.objectName().equals(pathTemp + "/")) {
                            item = itemTemp;
                        } else {
                            item = null;
                        }

                    }
                } catch (FileSystemException e) {
                    e.printStackTrace();
                }

            }
        }
        return item;
    }

    private String extractFilePath(String path) {
        String minioPath = path.substring(path.indexOf("/", 1) + 1);
        return minioPath;
    }

    private String extractBuket(String path) {
        String buket = "";
        if (0 != path.lastIndexOf("/")) {
            buket = path.substring(1, path.indexOf("/", 1));
        } else {
            buket = path.substring(1);
        }
        return buket;
    }


    @Override
    protected long doGetContentSize() throws Exception {
        return item == null ? 0 : item.size();
    }

    @Override
    protected FileType doGetType() throws Exception {
        //如果对象存在 返回对象的状态
        if (null != item) {
            //是文件就返回文件
            if (item.isDir()) {
                return FileType.FOLDER;
            } else {
                return FileType.FILE;
            }
        } else {
            //如果对象不存在 找到桶为FOLDER，桶之后的文件为IMAGINARY
            String buket = extractBuket(path.toString());
            String currentPath = path.toString();
            if (currentPath.length() == buket.length() + 1) {
                return FileType.FOLDER;
            } else {
                return FileType.IMAGINARY;
            }
        }
    }

    @Override
    protected String[] doListChildren() throws Exception {
        System.out.println("99999999999999999999");
        return new String[0];
    }

    @Override
    protected void doCreateFolder() throws Exception {
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(extractBuket(path.toString()))
                        .object(extractFilePath(path.toString()))
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }

    @Override
    public boolean exists() throws FileSystemException {
        return null != item;
    }

    @Override
    protected InputStream doGetInputStream() throws Exception {
        PutObjectArgs build = PutObjectArgs.builder().build();
        client.putObject(build);
        return build.stream();
    }

    @Override
    protected OutputStream doGetOutputStream(boolean bAppend) throws Exception {
        return super.doGetOutputStream(bAppend);
    }
}
