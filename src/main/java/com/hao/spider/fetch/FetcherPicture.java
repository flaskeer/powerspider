package com.hao.spider.fetch;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hao.spider.parse.JsoupParser;
import com.hao.spider.queue.Queue;
import com.hao.spider.queue.RedisQueue;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * please click main to run
 * Created by donghao on 16/7/16.
 */
@Slf4j
public class FetcherPicture{

    private static final String BASE_URL = "http://www.crtys.com/";
    private static AtomicInteger counter = new AtomicInteger(0);
    //defaultQueue   redisQueue
    private static Queue<String> imgUrlQueue = new RedisQueue<>();
    private static Executor executor = Executors.newFixedThreadPool(100);

    private static Document parse(String url) {
        return JsoupParser.parser(url,"");
    }

    public static void download(String imgUrl) {
        FileOutputStream fos = null;
        try {
            Connection.Response response = Jsoup.connect(imgUrl).ignoreContentType(true).timeout(60000).execute();
            byte[] bytes = response.bodyAsBytes();
            fos = new FileOutputStream(new File("/Users/donghao/img/pic" + counter.incrementAndGet() + ".jpg"));
            fos.write(bytes);
            log.info("download success,now the number is:{}",counter.get());
        } catch (IOException e) {
            download(imgUrl);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * 第一层链接
     * @return
     */
    private static List<String> firstLinks() {
        List<String> firstLinks = Lists.newArrayList();
        for (int i = 1; i <= 5 ; i++) {
            String firstLink = BASE_URL + "?action-category-catid-" + i;
            firstLinks.add(firstLink);
        }
        return firstLinks;
    }

    /**
     * 二级链接
     * @param firstLink
     * @return
     */
    private static List<String> secondLinks(String firstLink) {
        Document doc = parse(firstLink);
        List<String> secondLinks = Lists.newArrayList();
        Elements aElems = doc.select(".pages > div > a");
        String lastPage = aElems.get(aElems.size() - 2).text().replaceAll("\\.", "");
        int last = Integer.parseInt(lastPage);
        for (int i = 1; i <= last; i++) {
            String secondLink = BASE_URL + "?action-category-catid-1-page-" + i;
            secondLinks.add(secondLink);
        }
        return secondLinks;
    }

    /**
     * 三级链接
     * @param secondLink
     * @return
     */
    private static List<String> thirdLinks(String secondLink) {
        Document doc = parse(secondLink);
        List<String> thirdLinks = Lists.newArrayList();
        Elements liElems = doc.select(".pl > ul > li");
        liElems.forEach(liElem -> {
            String href = liElem.select("a").attr("href");
            String thirdLink = BASE_URL + href.substring(1);
            thirdLinks.add(thirdLink);
        });
        return thirdLinks;
    }


    /**
     *四级链接
     * @param thirdLinks
     * @return
     */
    private static List<String> fourLinks(String thirdLinks) {
        Document doc = parse(thirdLinks);
        List<String> fourLinks = Lists.newArrayList();
        Elements aElems = doc.select(".pages > div > a");
        String last = aElems.get(aElems.size() - 2).text();
        int lastPage = Integer.parseInt(last);
        for (int i = 1; i <= lastPage; i++) {
            String fourLink = thirdLinks + "-page-" + i;
            fourLinks.add(fourLink);
        }
        return fourLinks;
    }

    private static void downloadPictureLink(String fourLink) {
        Document doc = parse(fourLink);
        Elements pElems = doc.select(".imgbox > a > p");
        for (Element pElem : pElems) {
            String src = pElem.select("img").attr("src");
            log.info("img src:{}", src);
            imgUrlQueue.add(src);
        }

    }
    
    public static void downloadPicture() {
        String imgUrl = imgUrlQueue.push();
        if (!Strings.isNullOrEmpty(imgUrl)) {
            log.info("start downloading...url is :{}",imgUrl);
            download(imgUrl);
        }
    }




    public static void execute() {

        List<String> firstLinks = firstLinks();
        for (String firstLink : firstLinks) {
            List<String> secondLinks = secondLinks(firstLink);
            for (String secondLink : secondLinks) {
                List<String> thirdLinks = thirdLinks(secondLink);
                for (String thirdLink : thirdLinks) {
                    List<String> fourLinks = fourLinks(thirdLink);
                    fourLinks.parallelStream().forEach(FetcherPicture::downloadPictureLink);
                }
            }
        }

    }

    /**
     * for rx-spider
     * @return
     */
    public static List<String> allLinks() {
        List<String> allLinks = Lists.newArrayList();
        List<String> firstLinks = firstLinks();
        for (String firstLink : firstLinks) {
            List<String> secondLinks = secondLinks(firstLink);
            for (String secondLink : secondLinks) {
                List<String> thirdLinks = thirdLinks(secondLink);
                for (String thirdLink : thirdLinks) {
                    List<String> fourLinks = fourLinks(thirdLink);
                    fourLinks.forEach(allLinks::add);
                }
            }
        }
        return allLinks;
    }



    public static void submit(int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                while(true) {
                    try {
                        downloadPicture();
                    } catch (Exception e) {
                        //ignore
                    }
                }
            });
        }
    }


    public static void main(String[] args) {
        executor.execute(FetcherPicture::execute);
        submit(30);

    }

}
