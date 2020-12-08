package com.alan.databee.common;

import com.alan.databee.spider.exception.ScriptException;
import com.alan.databee.spider.exception.SpiderErrorEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptUtil {

    public static String getComName(String script) throws ScriptException {
        Pattern pattern = Pattern.compile("^public(?<type>\\w+|\\s+)class (?<name>\\w+)");
        Matcher matcher = pattern.matcher(script);
        int find = 0;
        String name = null;
        while (matcher.find()) {
            if (find >= 1) {
                throw new ScriptException(SpiderErrorEnum.Script_ClassNum_Error);
            }
            find++;
            name = matcher.group(2);
        }
        return name;
    }
}
