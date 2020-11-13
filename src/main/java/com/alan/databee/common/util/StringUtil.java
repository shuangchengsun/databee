package com.alan.databee.common.util;

public class StringUtil {
    public static boolean isEmpty(String string){
        return (string != null && string.length()>0);
    }
    public static boolean isBlank(String string){
        char[] chars = string.toCharArray();
        for(char ch : chars){
            if(ch != ' '){
                return false;
            }
        }
        return true;
    }
}
