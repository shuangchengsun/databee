package com.alan.databee;

import com.alan.databee.common.ScriptUtil;
import com.alan.databee.model.BusyReqModel;
import com.alan.databee.spider.exception.ScriptException;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class JsonTest {

    @Test
    public void toJson(){
        BusyReqModel model = new BusyReqModel();
        model.setBusyMsg("bugMsg");
        model.setDownloader("downloader");
        LinkedList<String> processor = new LinkedList<String>();
        processor.add("processor");
        model.setPageProcessor(processor);
//        model.setParam((Map<String, Object>) new HashMap<String,Object>().put("requestConfig","seedRequestConfig"));
        model.setScheduler("scheduler");
        List<String> pipeline = new LinkedList<>();
        pipeline.add("pipeline1");
        pipeline.add("pipeline2");
        model.setPipeline(pipeline);
        String s = JSON.toJSONString(model);
        System.out.println(s);
    }

    @Test
    public void ScriptUtilTest() throws ScriptException {
        String script = "\n" +
                "import com.alan.databee.common.util.log.LoggerUtil;\n" +
                "import com.alan.databee.spider.Site;\n" +
                "import com.alan.databee.spider.model.Request;\n" +
                "import com.alan.databee.spider.page.Page;\n" +
                "import com.alan.databee.spider.processor.PageProcessor;\n" +
                "import com.alan.databee.spider.selector.Json;\n" +
                "import com.jayway.jsonpath.PathNotFoundException;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "\n" +
                "import java.text.ParseException;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "import java.util.Calendar;\n" +
                "import java.util.Date;\n" +
                "import java.util.List;\n" +
                "\n" +
                "public class FJSeedProcessor implements PageProcessor {\n" +
                "    private static final Logger LOGGER = LoggerFactory.getLogger(\"PageProcessorLogger\");\n" +
                "\n" +
                "    @Override\n" +
                "    public void process(Page page, Site site) {\n" +
                "        Json json = page.getJson();\n" +
                "        List<String> all = json.jsonPath(\"$.docs[*]\").all();\n" +
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd\");\n" +
                "        boolean flag = true;\n" +
                "        int index = 0;\n" +
                "        try {\n" +
                "            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));\n" +
                "            Calendar calendar = Calendar.getInstance();\n" +
                "            calendar.setTime(today);\n" +
                "            int circle = site.getTaskCircle();\n" +
                "            calendar.add(Calendar.DATE, -circle);\n" +
                "            for (String a : all) {\n" +
                "                try {\n" +
                "                    Json node = new Json(a);\n" +
                "                    String title = node.jsonPath(\"$.title\").get();\n" +
                "                    String timeString = node.jsonPath(\"$.time\").get();\n" +
                "                    String href = node.jsonPath(\"$.url\").get();\n" +
                "                    String content = node.jsonPath(\"$.content\").get();\n" +
                "                    Date time = simpleDateFormat.parse(timeString);\n" +
                "                    Calendar current = Calendar.getInstance();\n" +
                "                    current.setTime(time);\n" +
                "                    if (current.after(calendar)) {\n" +
                "                        String m = \"url\" + \":\\t\" + href\n" +
                "                                + \",   title\" + \":\\t\" + title\n" +
                "                                + \",   content\" + \":\\t\" + content.replaceAll(\"<br>\", \"\");\n" +
                "                        page.putField(String.valueOf(index), m);\n" +
                "                        index++;\n" +
                "                    } else {\n" +
                "                        flag = false;\n" +
                "//                        System.out.println(\"time: \" + timeString + \", href: \" + href + \", title: \" + title);\n" +
                "                    }\n" +
                "                } catch (PathNotFoundException e) {\n" +
                "                    LoggerUtil.error(LOGGER, \"内容解析错误\", a, page.getUrl());\n" +
                "                }\n" +
                "\n" +
                "            }\n" +
                "            if (flag) {\n" +
                "                Request request = page.getRequest();\n" +
                "                String url = request.getUrl();\n" +
                "                int length = url.length();\n" +
                "                int point = length - 1;\n" +
                "                for (; point >= 0; point--) {\n" +
                "                    if (url.charAt(point) == '=') {\n" +
                "                        break;\n" +
                "                    }\n" +
                "                }\n" +
                "                int param = Integer.parseInt(url.substring(point + 1, length));\n" +
                "                String newUrl = url.substring(0, point + 1) + (param + 1);\n" +
                "                request.setUrl(newUrl);\n" +
                "                page.addTargetRequest(request);\n" +
                "            }\n" +
                "            LoggerUtil.info(LOGGER, \"页面解析完成\", page.getUrl());\n" +
                "        } catch (ParseException e) {\n" +
                "            LoggerUtil.warn(LOGGER, \"时间解析错误\", page.getUrl());\n" +
                "        }\n" +
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

        String comName = ScriptUtil.getComName(script);
        System.out.println(comName);
    }
}
