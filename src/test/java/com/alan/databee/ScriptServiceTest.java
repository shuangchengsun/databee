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
            "import com.alan.databee.spider.Site;\n" +
            "import com.alan.databee.spider.page.Page;\n" +
            "import com.alan.databee.spider.processor.PageProcessor;\n" +
            "import com.alan.databee.spider.selector.Html;\n" +
            "import com.alan.databee.spider.selector.Selectable;\n" +
            "import org.slf4j.Logger;\n" +
            "import org.slf4j.LoggerFactory;\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.regex.Matcher;\n" +
            "import java.util.regex.Pattern;\n" +
            "\n" +
            "public class HBContentProcessor implements PageProcessor {\n" +
            "    private static final Logger LOGGER = LoggerFactory.getLogger(\"PageProcessorLogger\");\n" +
            "    @Override\n" +
            "    public void process(Page page, Site site) {\n" +
            "        Html html = page.getHtml();\n" +
            "        Selectable titleXpath = html.xpath(\"//h2[@class='text-center']\");\n" +
            "        String title = titleXpath.get();\n" +
            "        page.putField(\"title\",title);\n" +
            "        page.putField(\"url\",page.getUrl());\n" +
            "        Selectable contentXpath = html.xpath(\"//div[@class='row content_block']/div[@class='col-xs-12 xs_nopad_md_pad']/div[@class='view TRS_UEDITOR trs_paper_default trs_web']/p\");\n" +
            "        List<String> all = contentXpath.all();\n" +
            "        StringBuilder builder = new StringBuilder();\n" +
            "        for(String s:all){\n" +
            "            Pattern pattern = Pattern.compile(\"<p style=(.*?)\\\">(.*?)</p>\");\n" +
            "            Matcher matcher = pattern.matcher(s);\n" +
            "            if(matcher.find()){\n" +
            "                builder.append(matcher.group(2));\n" +
            "            }else {\n" +
            "                LoggerUtil.warn(LOGGER,\"匹配出错\",page.getUrl());\n" +
            "            }\n" +
            "        }\n" +
            "        String content = builder.toString();\n" +
            "        page.putField(\"content\",content);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Site getSite() {\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void setSite() {\n" +
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
            Class<?> aClass = scriptService.genClass("HBContentProcessor", code);
            System.out.println(aClass.getName());
        }catch (ScriptException exception){
            LoggerUtil.error(LOGGER, SpiderErrorEnum.Script_Compiler_Error,exception);
        }
    }

}
