package com.zyk;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Test;

/**
 * @author 凯少
 * @create 2022-09-14 10:07
 */
public class HttpApiTest {

    @Test
    public void httpApiTest() throws Exception {
        FileSystemManager manager = VFS.getManager();
        String uri = "http://root:rootroot@192.168.110.102:9001/buckets/test-bucket/browse";
        FileObject fileObject = manager.resolveFile(uri);
        FileObject[] children = fileObject.getChildren();
        System.out.println("children.length = " + children.length);
    }
}
