package com.hikarios.elasticsearch.esjd.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(@Value("${elasticsearch.node.path:http://127.0.0.1:9200}") String path){
        System.out.println("-------"+path);
        String[] split = path.split(":");
        String scheme = split[0];
        String host = split[1].split("//")[1];
        Integer port = Integer.valueOf(split[2]);
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host,port,scheme))
        );
        return client;
    }
}