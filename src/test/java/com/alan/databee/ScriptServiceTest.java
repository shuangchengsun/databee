package com.alan.databee;

import com.alan.databee.common.util.log.LoggerUtil;
import com.alan.databee.service.ClassService;
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
    String code = "import com.alan.databee.common.util.log.LoggerUtil;\n" +
            "import com.alan.databee.spider.Task;\n" +
            "import com.alan.databee.spider.model.ResultItems;\n" +
            "import com.alan.databee.spider.pipeline.Pipeline;\n" +
            "import org.slf4j.Logger;\n" +
            "import org.slf4j.LoggerFactory;\n" +
            "\n" +
            "import java.util.Map;\n" +
            "import java.util.regex.Matcher;\n" +
            "import java.util.regex.Pattern;\n" +
            "\n" +
            "public class FxyPipeline implements Pipeline {\n" +
            "    private static final Logger LOGGER = LoggerFactory.getLogger(\"FxyLogger\");\n" +
            "    @Override\n" +
            "    public void process(ResultItems resultItems, Task task) {\n" +
            "\n" +
            "        String content = resultItems.get(\"content\");\n" +
            "        if(content!= null){\n" +
            "            LoggerUtil.info(LOGGER,\"url:   \"+resultItems.get(\"url\"),\"title:   \"+resultItems.get(\"title\"));\n" +
            "        }else {\n" +
            "            StringBuilder builder = new StringBuilder();\n" +
            "            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {\n" +
            "                builder.append(entry.getKey()).append(\":\\t\").append(entry.getValue());\n" +
            "            }\n" +
            "            String s = builder.toString();\n" +
            "            Matcher urlMatcher = Pattern.compile(\"[a-zA-z]+://[^\\\\s]*\").matcher(s);\n" +
            "            Matcher titleMatcher = Pattern.compile(\"title:(.*?)content\").matcher(s);\n" +
            "            String url = null;\n" +
            "            String title = null;\n" +
            "            if(urlMatcher.find()){\n" +
            "                url = urlMatcher.group();\n" +
            "            }\n" +
            "            if(titleMatcher.find()){\n" +
            "                title = titleMatcher.group();\n" +
            "            }\n" +
            "            LoggerUtil.info(LOGGER,\"url:   \"+url,\"title:   \"+title);\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "}\n";
    @Autowired
    ScriptService scriptService;

    @Autowired
    ClassService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptServiceTest.class);

    @Test
    public void testCompiler(){
        try {
            Class<?> aClass = scriptService.genClass("FxyPipeline", code);
            System.out.println(aClass.getName());
        }catch (ScriptException exception){
            LoggerUtil.error(LOGGER, SpiderErrorEnum.Script_Compiler_Error,exception);
        }
    }

}
