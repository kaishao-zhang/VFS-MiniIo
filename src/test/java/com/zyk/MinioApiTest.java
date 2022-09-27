package com.zyk;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Tags;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

/**
 * @author 凯少
 * @create 2022-09-09 14:17
 */
public class MinioApiTest {
    MinioClient minioClient = null;


    @Before
    public void getClient() {
        minioClient = MinioClient.builder().endpoint("http://192.168.136.102:9000").credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY").build();
    }
//----------------------------------------------bucket----------------------------------------------------

    /**
     * 获取minio的所有桶列表
     */
    @Test
    public void listBuckets() throws Exception {
        List<Bucket> bucketList = minioClient.listBuckets();
        for (Bucket bucket : bucketList) {
            System.out.println(bucket.creationDate() + ", " + bucket.name());
        }
    }
//----------------------------------------------Object----------------------------------------------------

    /**
     * 递归获取所有的文件对象
     */
    @Test
    public void listObjects() throws Exception {
        // 递归遍历所有的信息
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket("test-bucket").recursive(true).build());
//        Iterable<Result<Item>> results = minioClient.listObjects(
//                ListObjectsArgs.builder().bucket("test-bucket").build());
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

    /**
     * 获取单个文件对象
     */
    @Test
    public void getObject() throws Exception {
        String url = "";
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket("test-bucket")
//                        .prefix("a/b.txt")
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

    /**
     * 上传文件
     */
    @Test
    public void uploadObject() throws Exception {
        //创建一个目录
//        minioClient.putObject(
//                PutObjectArgs.builder()
//                        .bucket("test-bucket")
//                        .object("b/")
//                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
//                        .build());
//        minioClient.uploadObject(UploadObjectArgs.builder().bucket("test-bucket").object("b/a.txt").filename("C:\\Users\\14848\\Desktop\\a.txt").build());
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("test-bucket")
                        .object("b/")
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }

    @Test
    public void statObject()throws Exception{
        StatObjectResponse statObject = minioClient.statObject(
                StatObjectArgs.builder().bucket("test-bucket").object("b/").build());
        System.out.println("statObject.deleteMarker() = " + statObject.deleteMarker());
        System.out.println("statObject.size() = " + statObject.size());
        System.out.println("statObject.contentType() = " + statObject.contentType());
        System.out.println("statObject.size() = " + statObject.size());
        System.out.println("statObject.lastModified() = " + statObject.lastModified());
        System.out.println("statObject.etag() = " + statObject.etag());
        System.out.println("statObject.legalHold() = " + statObject.legalHold().status());
        System.out.println("statObject.object() = " + statObject.object());
    }
}