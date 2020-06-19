package com.hikarios.elasticsearch.esjd.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hikarios.elasticsearch.esjd.EsJdApplication;
import com.hikarios.elasticsearch.esjd.entity.Content;
import com.hikarios.elasticsearch.esjd.util.ElasticsearchUtil;
import com.hikarios.elasticsearch.esjd.util.HtmlParseUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    @NonNull
    private RestHighLevelClient client;

    public Boolean parseContent(String keyword) throws IOException {
        ElasticsearchUtil esUtil = new ElasticsearchUtil(client);
        List<Content> contents = HtmlParseUtil.parseJd(keyword);
        List<JSONObject> collect = contents.stream().map(JSONUtil::parseObj).collect(Collectors.toList());
        return esUtil.bulkDocument(EsJdApplication.INDEX_NAME,collect);
    }


    /**
     * 搜索条件
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     * @throws IOException
     */
    public List<Map<String,Object>> searchPage(Integer pageNum, Integer pageSize, String keyword) throws IOException {
        if (pageNum<=0) pageNum =1;
//        Integer jumpNum = (pageNum-1) * pageSize;
        SearchRequest request = new SearchRequest(EsJdApplication.INDEX_NAME);
        SearchSourceBuilder builder = new SearchSourceBuilder();

        builder.from(pageNum);
        builder.size(pageSize);


        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name",keyword);
        builder.query(termQueryBuilder);
        builder.timeout(new TimeValue(60,TimeUnit.SECONDS));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.requireFieldMatch(false);
        builder.highlighter(highlightBuilder);

        request.source(builder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        List<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField name = highlightFields.get("name");
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
            if (name!=null){
                Text[] fragments = name.getFragments();
                StringBuilder stringBuilder = new StringBuilder();
                for (Text fragment : fragments) {
                    stringBuilder.append(fragment);
                }
                sourceAsMap.put("name",stringBuilder.toString());
            }
            list.add(sourceAsMap);
        }
        return list;
    }
}
