package com.alan.databee;

import com.alan.databee.service.SpiderManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SpiderTest
 * @Author sunshuangcheng
 * @Date 2020/10/31 11:11 下午
 * @Version -V1.0
 */
@SpringBootTest
public class SpiderTest {

//    @Autowired
//    SpiderManager manager;

    @Test
    public void spiderManagerTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE,-7);
        Date time = simpleDateFormat.parse("2020-12-02 22:12:03.352");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time);
        System.out.println(calendar1.after(calendar));
    }
}
