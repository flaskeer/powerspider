package com.hao.spider.parse;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 *
 * download html page  and send to jsoup for parsing
 * @author dongh38@ziroom
 * @Date 16/9/24
 * @Time 上午11:49
 */
public interface HtmlPage {

    @RequestLine("GET /{url}")
    @Headers("Content-Type:text/html")
    String getHtmlPage(@Param("url") String url);
}
