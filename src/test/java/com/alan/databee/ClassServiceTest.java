package com.alan.databee;



import com.alan.databee.dao.mapper.ComponentConfigMapper;
import com.alan.databee.service.ClassService;
import com.alan.databee.spider.exception.ClassServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClassServiceTest {

    @Autowired
    ClassService classService;
    @Autowired(required = false)
    ComponentConfigMapper mapper;

    @Test
    public void basicTest() throws ClassServiceException {
        Object jxContentProcessor = classService.getComByName("JXContentProcessor");
        String name = jxContentProcessor.getClass().getName();
        System.out.println(name);
    }
}
