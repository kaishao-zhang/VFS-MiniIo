package com.zyk;

import com.hxbcc.commons.vfs2.provider.MinioClientFactory;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.vfs2.*;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

/**
 * @author 凯少
 * @create 2022-08-26 16:13
 */
public class MinioTest {
        public static void main(String[] args)
            throws Exception {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://192.168.0.101:9000")
                            .credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                            .build();

            // Make 'asiatrip' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("test-bucket").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("test-bucket").build());
            } else {
                System.out.println("Bucket 'test-bucket' already exists.");
            }

            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("test-bucket")
                            .object("a.txt")
                            .filename("C:\\Users\\14848\\Desktop\\a.txt")
                            .build());
            System.out.println(
                    "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
                            + "object 'asiaphotos-2015.zip' to bucket 'asiatrip'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
    @Test
    public void test() throws FileSystemException {
        FileSystemManager fsManager = VFS.getManager();
        FileObject jarFile = fsManager.resolveFile("minio://root:rootroot@192.168.126.102:9000/test-bucket/a/a.txt");
        System.out.println("jarFile.getName().getBaseName() = " + jarFile.getName().getBaseName());
        FileType type = jarFile.getType();
        System.out.println("type = " + type);
        System.out.println("jarFile = " + jarFile);
        jarFile.createFile();
    }

    @Test
    public void testFactory() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClientFactory.createConnection("http", "192.168.136.102", 9000
                , "root", "rootroot", null);
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket("test-bucket")
                        .prefix("a/")
                        .build()
        );
        Iterator<Result<Item>> iterator = results.iterator();
        while (iterator.hasNext()) {
            System.out.println("-------------------1------------------------");
            Result<Item> next = iterator.next();
            Item item = next.get();
            System.out.println("item = " + item);
            System.out.println("item.etag() = " + item.etag());
            System.out.println("item.isDeleteMarker() = " + item.isDeleteMarker());
            System.out.println("item.isDir() = " + item.isDir());
            System.out.println("item.objectName() = " + item.objectName());
            if (!item.isDir()) {
                System.out.println("item.owner().displayName() = " + item.owner().displayName());
                System.out.println("item.size() = " + item.size());
            }
            System.out.println("----------------------------------------");
        }
    }

}
