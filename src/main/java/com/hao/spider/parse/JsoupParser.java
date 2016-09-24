package com.hao.spider.parse;

import com.hao.spider.http.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author dongh38@ziroom
 * @Date 16/9/24
 * @Time 下午12:04
 */
public class JsoupParser {

    public static Document parser(String absUrl,String prefixUrl) {
        HtmlPage htmlPage = HttpClient.getInstance().action(HtmlPage.class, absUrl);
        String page = htmlPage.getHtmlPage(prefixUrl);
        return Jsoup.parse(page);
    }

}
