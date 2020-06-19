package com.hikarios.elasticsearch.esjd;

import com.hikarios.elasticsearch.esjd.util.ElasticsearchUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@SpringBootApplication
public class EsJdApplication {

    public  static final String INDEX_NAME = "jd_goods";

    public static void main(String[] args) {
        SpringApplication.run(EsJdApplication.class, args);
    }

    @Resource
    private RestHighLevelClient client;

    @PostConstruct
    public void initIndex(){
        ElasticsearchUtil util = new ElasticsearchUtil(client);
        util.createIndex(INDEX_NAME);
    }
}
