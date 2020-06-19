package com.hikarios.elasticsearch.esjd;

import com.hikarios.elasticsearch.esjd.service.ContentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class EsJdApplicationTests {

    @Autowired
    private ContentService contentService;
    @Test
    void contextLoads() throws IOException {
        Boolean flag = contentService.parseContent("java");
        if (flag){
            System.out.println("成功");
        }
    }

}
