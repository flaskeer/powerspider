package com.hao.spider.parse;

import com.hao.spider.http.HttpClient;
import feign.Feign;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author dongh38@ziroom
 * @Date 16/9/24
 * @Time 下午12:04
 */
public class JsoupParser {

    public static Document parser(String absUrl,String prefixUrl) {
        HtmlPage htmlPage = Feign.builder().target(HtmlPage.class, absUrl);
        String page = htmlPage.getHtmlPage(prefixUrl);
        return Jsoup.parse(page);
    }

}
