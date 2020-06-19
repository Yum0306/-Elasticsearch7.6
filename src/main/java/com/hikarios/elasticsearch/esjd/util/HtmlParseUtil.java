package com.hikarios.elasticsearch.esjd.util;

import com.hikarios.elasticsearch.esjd.entity.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlParseUtil {

    public static List<Content> parseJd(String keyword) throws IOException {
        List<Content> list = new ArrayList<>();
        String url = "https://search.jd.com/Search?keyword="+keyword+"&enc=utf-8";
        Document root = Jsoup.parse(new URL(new String(url.getBytes(),"utf-8")), 30000);
        Element j_goodsList = root.getElementById("J_goodsList");
        Elements li = j_goodsList.getElementsByTag("li");
        for (Element element : li) {
            String src = element.getElementsByTag("img").eq(0).attr("src");
            String price = element.getElementsByClass("p-price").eq(0).text();
            String name = element.getElementsByClass("p-name").eq(0).text();
            list.add(new Content(src,price,name));
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        List<Content> list = parseJd("心理学");
        list.forEach(System.out::println);
    }
}
