package com.hao.spider.parse;

import feign.Feign;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author dongh38@ziroom
 * @Date 16/9/24
 * @Time 下午12:04
 */
public class JsoupParser {

    public static Document parser(String baseUrl,String prefixUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            return null;
        }
        HtmlPage htmlPage = Feign.builder().target(HtmlPage.class, baseUrl);
        String page = htmlPage.getHtmlPage(prefixUrl);
        return Jsoup.parse(page);
    }

}
