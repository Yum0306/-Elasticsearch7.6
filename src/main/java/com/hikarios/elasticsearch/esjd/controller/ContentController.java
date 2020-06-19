package com.hikarios.elasticsearch.esjd.controller;

import cn.hutool.json.JSONUtil;
import com.hikarios.elasticsearch.esjd.service.ContentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ContentController {

    @NonNull
    private ContentService contentService;

    /**
     * 解析 并存储到es中
     * @param keyword
     * @return
     * @throws IOException
     */
    @GetMapping("/parse/{keyword}")
    public String parse(@PathVariable String keyword) throws IOException {
        Boolean flag = contentService.parseContent(keyword);
        if (flag){
            return "操作成功";
        }else {
            return "操作失败";
        }
    }

    /**
     * 分页搜索
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/search/{keyword}/{pageNum}/{pageSize}")
    public String searchPage(@PathVariable String keyword, @PathVariable Integer pageNum, @PathVariable Integer pageSize) throws IOException {
        log.warn("接受到请求");
        List<Map<String, Object>> list = contentService.searchPage(pageNum, pageSize, keyword);
        return JSONUtil.toJsonStr(list);
    }
}
