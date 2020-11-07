package com.alan.databee;

import com.alan.databee.spider.script.ScriptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @ClassName ScriptServiceTest
 * @Author sunshuangcheng
 * @Date 2020/11/1 3:20 下午
 * @Version -V1.0
 */
@SpringBootTest
public class ScriptServiceTest {
    String code = "import us.codecraft.webmagic.ResultItems;\n" +
            "import us.codecraft.webmagic.Task;\n" +
            "import us.codecraft.webmagic.pipeline.Pipeline;\n" +
            "\n" +
            "import java.util.Map;\n" +
            "\n" +
            "/**\n" +
            " * Write results in console.<br>\n" +
            " * Usually used in test.\n" +
            " *\n" +
            " * @author code4crafter@gmail.com <br>\n" +
            " * @since 0.1.0\n" +
            " */\n" +
            "public class ConsolePipeline implements Pipeline {\n" +
            "\n" +
            "    @Override\n" +
            "    public void process(ResultItems resultItems, Task task) {\n" +
            "        System.out.println(\"get page: \" + resultItems.getRequest().getUrl());\n" +
            "        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {\n" +
            "            System.out.println(entry.getKey() + \":\\t\" + entry.getValue());\n" +
            "        }\n" +
            "    }\n" +
            "}";
    @Autowired
    ScriptService scriptService;

    @Test
    public void testCompiler(){
        Map<String, Class<?>> consolePipeline = scriptService.genClass("ConsolePipeline", code);
        Class<?> aClass = consolePipeline.get("ConsolePipeline");
    }

}
