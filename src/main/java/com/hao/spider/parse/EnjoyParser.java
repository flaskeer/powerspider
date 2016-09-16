package com.hao.spider.parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hao.spider.Model.Food;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static com.hao.spider.constants.Constants.*;
/**
 * @author dongh
 * @Date 16/9/16
 * @Time 上午10:57
 */
public class EnjoyParser {

    private static AtomicInteger count = new AtomicInteger(0);




    public static Document getDocument(String url) throws IOException {
        return getDocument(url,6000);
    }

    public static String getJson(String url,int timeout) throws IOException {
        String body = null;
        try {
            body = Jsoup.connect(url)
                    .timeout(timeout)
                    .ignoreContentType(true)
                    .header("Cookie", "connect.sid=s%3AKBKP-rYoXCuqFRrTUnadJfiODBB_9Jma.Sxy%2BmiFuhf7O4EuwTnlssmsF8188byNMgvkVfG0ci4A; rb_city=140; Hm_lvt_6ad0f9786e9035e0275f33c495e02f52=1473994512; Hm_lpvt_6ad0f9786e9035e0275f33c495e02f52=1473995526")
                    .header("Host", "enjoy.ricebook.com")
                    .header("Upgrade-Insecure-Requests", "1")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                    .execute().body();
        } catch (IOException e) {
            if (count.incrementAndGet() < retryTime) {
                getJson(url, timeout);
            } else {
                count = new AtomicInteger(0);
                throw new IOException(e);
            }
        }
        return body;
    }

    public static String getJson(String url) throws IOException {
        return getJson(url,6000);
    }


    public static Document getDocument(String url,int timeout) throws IOException {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                            .timeout(timeout)
                            .ignoreContentType(true)
                            .header("Cookie","connect.sid=s%3AKBKP-rYoXCuqFRrTUnadJfiODBB_9Jma.Sxy%2BmiFuhf7O4EuwTnlssmsF8188byNMgvkVfG0ci4A; rb_city=140; Hm_lvt_6ad0f9786e9035e0275f33c495e02f52=1473994512; Hm_lpvt_6ad0f9786e9035e0275f33c495e02f52=1473995526")
                            .header("Host","enjoy.ricebook.com")
                            .header("Upgrade-Insecure-Requests","1")
                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                            .get();
        } catch (IOException e) {
            if (count.incrementAndGet() < retryTime) {
                getDocument(url,timeout);
            } else {
                count = new AtomicInteger(0);
                throw new IOException(e);
            }
        }
        return document;
    }

    /**
     * parse  home page   . in fact the data is transported by json
     * @param url
     * @return
     * @throws IOException
     */
    public static List<Food> homepageParse(String url) throws IOException {
        List<Food> foodList = Lists.newArrayList();
        Document doc = getDocument(url);
        Food food = new Food();
        Elements prodElems = doc.select("#product > li");
        prodElems.forEach(prodElem -> {
            food.setImgSrc(prodElem.select("a > div > img").attr("src"));
            food.setInfo(prodElem.select(".info").text());
            food.setDesc(prodElem.select(".description > p").text());
            food.setUnit(prodElem.select(".unit").text());
            food.setPrice(Double.parseDouble(prodElem.select(".price").text().replaceAll("元","")));
            foodList.add(food);
        });
        return foodList;
    }

    /**
     * parse json data
     * @param url
     */
    public static void jsonParse(String url) throws IOException {
        String json = getJson(url);
        JSONArray jsonArray = JSON.parseArray(json);
        List<Food> foodList = Lists.newArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Food food = new Food();
            food.setImgSrc(jsonObject.getString("product_image"));
            food.setInfo(jsonObject.getString("name"));
            food.setDesc(jsonObject.getString("short_description"));
            food.setUnit(jsonObject.getString("entity_name"));
            food.setPrice(jsonObject.getDouble("price"));
            foodList.add(food);
        }
        System.out.println(foodList);
    }

    public static void main(String[] args) {
        try {
//            List<Food> foodList = homepageParse("http://enjoy.ricebook.com/");
            jsonParse("http://enjoy.ricebook.com/innerpage/140,5,0");
//            System.out.println(foodList);
//            System.out.println(foodList.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
