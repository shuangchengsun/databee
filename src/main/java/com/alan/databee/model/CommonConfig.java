package com.alan.databee.model;

import java.util.HashSet;
import java.util.Set;

public class CommonConfig {
    static Set<String> commonComponent = new HashSet<>();

    public static void addComponent(String name){
        commonComponent.add(name);
    }
    public static boolean isContainCom(String name){
        return commonComponent.contains(name);
    }
}
