package com.zyk;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.hdfs.HdfsFileSystemConfigBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.net.URI;

/**
 * @author 凯少
 * @create 2022-09-13 13:22
 */
public class HDFSApiTest {

    @Test
    public void hdfsVfsApiTest() throws Exception {
        String url = "hdfs://root:@hadoop1:9009/4.txt";
        FileSystemManager manager = VFS.getManager();
        HdfsFileSystemConfigBuilder instance = HdfsFileSystemConfigBuilder.getInstance();
        FileSystemOptions fileSystemOptions = new FileSystemOptions();
        instance.setConfigName(fileSystemOptions,"");
        FileObject fileObject = manager.resolveFile(url,fileSystemOptions);
        String url1 = "hdfs://root:@hadoop1:9009/home/hxbop/zyk/yarn.cmd";
        fileObject.copyFrom(manager.resolveFile(url1), new AllFileSelector());
//        FileObject[] children = fileObject.getChildren();
//        if (null != children && children.length > 0) {
//            for (int i = 0; i < children.length; i++) {
//                FileObject child = children[i];
//                System.out.println("child.getName() = " + child.getName());
//                System.out.println("child.isFolder() = " + child.isFolder());
////                child.getFileOperations()
//            }
//        }
    }

    @Test
    public void vfsApiTest() throws Exception {
        //创建连接
        //获取主节点
        URI uri = new URI("hdfs://hadoop1:9009/");
        Configuration entries = new Configuration(true);
        entries.addResource("hdfs://hadoop1:9009/");
        entries.set("hadoop.security.service.user.name.key","hxbop");
        //连接的地址 参数  用户
        FileSystem root = FileSystem.get(uri,entries,"hxbop");

        //操作hdfs
        //create 创建一个文件 Path 指定路径 String 类型
        root.create(new Path("/home/hxbop/zyk/2.txt"));
        //关流
        root.close();
    }
}
