package com.alan.databee.wangyiMusic;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
public class WangyiVideoTest {

    @Test
    public void test() throws IOException {
        BinaryDownloader downloader = new BinaryDownloader();
        byte[] download = downloader.downloadByJs("https://vodkgeyttp8.vod.126.net/cloudmusic/MTAiYDQwICQwMDRkOTAhIg==/mv/5439044/40ea656798cfb56e484bde83974ba785.mp4?wsSecret=6c13bc1225fad36ed86bbb039fbb9ac9&wsTime=1606396200");
//        File file = new File("mv-163-2.mp4");
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
//        bufferedOutputStream.write(download);
    }

}
