package com.alan.databee;

import com.alan.databee.spider.SpiderManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SpiderTest
 * @Author sunshuangcheng
 * @Date 2020/10/31 11:11 下午
 * @Version -V1.0
 */
@SpringBootTest
public class SpiderTest {

    @Autowired
    SpiderManager manager;

    @Test
    public void spiderManagerTest(){
        manager.runTask();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
