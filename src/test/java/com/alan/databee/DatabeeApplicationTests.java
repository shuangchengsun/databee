package com.alan.databee;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@SpringBootTest
class DatabeeApplicationTests {

    @Test
    public void jsonTest(){
        List<String>  list = new ArrayList<>();
        list.add("ConsolePipeline");
        String string = JSON.toJSONString(list);
        System.out.println(string);

    }

}

