package com.alan.databee;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.spider.exception.ScriptException;
import com.alan.databee.spider.exception.SpiderErrorEnum;
import com.alan.databee.spider.script.ScriptService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            "";
    @Autowired
    ScriptService scriptService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptServiceTest.class);

    @Test
    public void testCompiler(){
        try {
            Class<?> consolePipeline = scriptService.genClass("ConsolePipeline", code);
        }catch (ScriptException exception){
            LoggerUtil.error(LOGGER, SpiderErrorEnum.Script_Compiler_Error,exception);
        }
    }

}
