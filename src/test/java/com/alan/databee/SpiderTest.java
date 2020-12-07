package com.alan.databee;

import com.alan.databee.service.SpiderManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    public void spiderManagerTest(){
        String s = "0:\turl:\thttp://www.fujian.gov.cn/xwdt/fjyw/202012/t20201207_5474736.htm,   title:\t深入贯彻落实习近平总书记重要讲话重要指示精神<br>奋力夺取疫情防控和实现经济社会发展目标双胜利,   content:\t\n" +
                "　　12月6日，省委书记尹力赴厦门调研时强调，要深入学习贯彻习近平总书记重要讲话和党的十九届五中全会精神，始终牢记习近平总书记对厦门的殷殷嘱托，全面把握新发展阶段，坚定不移贯彻新发展理念，服务构建新发展格局，充分用好中央在厦门建设金砖国家新工业革命伙伴关系创新基地的重大机遇，大力实施跨岛发展战略，在更高起点上建设高素质高颜值现代化国际化城市，奋力夺取疫情防控和实现经济社会发展目标双胜利。省长王宁一同调研。\n" +
                "　　尹力一行首先来到厦门高崎国际机场T3航站楼\n" +
                "1:\turl:\thttp://www.fujian.gov.cn/xwdt/fjyw/202012/t20201207_5474737.htm,   title:\t为民办实事项目省以上资金全部下达,   content:\t\n" +
                "　　据省财政厅消息，截至11月底，省委省政府确定的28个为民办实事项目省以上财政投入已全部下达，总金额达167.75亿元，完成计划投入的114.2%。\n" +
                "　　今年我省28个为民办实事项目，在延续实施2019年部分项目的基础上，新增了6个项目，分别是巩固提升农村饮水安全、提高医疗服务供给能力和水平、开展婴幼儿照护服务试点、实施老旧小区改造工程、推行垃圾分类以及保护利用城乡历史文化。（记者&nbsp;王永珍）\n" +
                "2:\turl:\thttp://www.fujian.gov.cn/xwdt/fjyw/202012/t20201207_5474739.htm,   title:\t自立自强，打造区域创新高地,   content:\t\n" +
                "　　人工智能、区块链、石墨烯、高效储能、定位导航……近年来，一批具有前瞻性、战略性的科技项目在我省布局。\n" +
                "　　党的十九届五中全会提出，坚持创新在我国现代化建设全局中的核心地位，把科技自立自强作为国家发展的战略支撑。在福建，科技自立自强能力如何提高？\n" +
                "　　中科院海西研究院员工认真学习贯彻五中全会精神，大家表示，实现科技自立自强，就要增强自主创新能力，在加强应用技术攻关的同时，更加重视具有前沿性的基础科学研究，努力在关键核心领域实现可持续发展。\n" +
                "　　“\n" +
                "3:\turl:\thttp://www.fujian.gov.cn/xwdt/fjyw/202012/t20201207_5474740.htm,   title:\t八地获评全国节水型社会建设达标县（区）,   content:\t\n" +
                "　　近日，记者从省水利厅获悉，永泰县、莆田市城厢区、沙县、永春县、龙海市、松溪县、龙岩市新罗区、寿宁县等八地获评全国第三批节水型社会建设达标县（区）。\n" +
                "　　近年来，我省高度重视节水工作，坚决落实最严格水资源管理制度，突出节水优先，建立节水协作推动机制，全力推进节水行动落地见效。2019年，我省用水总量177.3亿立方米，比2016年减少11.8亿立方米；万元GDP用水量、万元工业增加值用水量均比2015年下降35%以上。（记者&nbsp;张静雯）\n";
        Matcher urlMatcher = Pattern.compile("[a-zA-z]+://[^\\s]*").matcher(s);
        Matcher titleMatcher = Pattern.compile("title:(.*?)content").matcher(s);
        String url = null;
        String title = null;
        while(urlMatcher.find() && titleMatcher.find()){
            url = urlMatcher.group();
            title = titleMatcher.group(1);
            System.out.println("url:   " + url+ ",   title:   " + title);

        }

    }
}
