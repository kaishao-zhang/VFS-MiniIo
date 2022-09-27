package com.zyk;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Test;

/**
 * @author 凯少
 * @create 2022-09-16 10:56
 */
public class VFSApiTest {

    @Test
    public void testVFSApi() throws Exception {
//        String filePath = "file://C:\\Users\\14848\\Desktop\\a.txt";
        FileSystemManager manager = VFS.getManager();
//        String filePath1 = "file://C:\\Users\\14848\\Desktop\\b.txt";
//        FileObject fileObject = manager.resolveFile(filePath);
//        FileObject fileObject1 = manager.resolveFile(filePath1);
//        fileObject1.copyFrom(fileObject, new AllFileSelector());
//        System.out.println("fileObject.isFolder() = " + fileObject.isFolder());

        String createFile = "file://C:\\Users\\14848\\Desktop\\zyktest\\c.txt";
        FileObject fileObject2 = manager.resolveFile(createFile);
        fileObject2.createFile();

    }

//    @Test
//    public void testVFSApi() throws Exception {
//        String filePath = "file://C:\\Users\\14848\\Desktop\\a.txt";
//        FileSystemManager manager = VFS.getManager();
//        String filePath1 = "file://C:\\Users\\14848\\Desktop\\b.txt";
//        FileObject fileObject = manager.resolveFile(filePath);
//        FileObject fileObject1 = manager.resolveFile(filePath1);
//        fileObject1.copyFrom(fileObject, new AllFileSelector());
//        System.out.println("fileObject.isFolder() = " + fileObject.isFolder());
//    }
}
