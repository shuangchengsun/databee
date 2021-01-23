package com.alan.databee.FanRenXiuXianZhuan.pipeline;

import com.alan.databee.spider.Task;
import com.alan.databee.spider.model.ResultItems;
import com.alan.databee.spider.pipeline.Pipeline;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileWritePipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");
        String content = resultItems.get("content");
        content = "### "+title+"\r\n"+"\r\n"+content;
        String path = System.getProperty("user.dir") + "/article/" + title+".md";
        File file = new File(path);
        try {
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
