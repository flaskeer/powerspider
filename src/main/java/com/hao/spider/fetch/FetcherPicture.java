package com.hao.spider.fetch;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hao.spider.queue.Queue;
import com.hao.spider.queue.RedisQueue;
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
 * Created by donghao on 16/7/16.
 */
public class FetcherPicture{

    private static final String BASE_URL = "http://www.crtys.com/";
    private static AtomicInteger counter = new AtomicInteger(0);
    private static Queue<String> imgUrlQueue = new RedisQueue<>();
    private static Executor executor = Executors.newFixedThreadPool(100);

    private static Document parse(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                    .header("Host", "www.crtys.com")
                    .header("Referer","http://www.crtys.com/")
                    .cookie("Hm_lvt_60a472097feb0e488430d0b13467a3aa","1468674446")
                    .cookie("Hm_lpvt_60a472097feb0e488430d0b13467a3aa","1468674907")
                    .cookie("supe_supe_refresh_items","0_1826")
                    .timeout(60000)
                    .ignoreContentType(true)
                    .get();
        } catch (IOException e) {
           doc = parse(url);
        }
        return doc;
    }

    private static void download(String imgUrl) {
        FileOutputStream fos = null;
        try {
            Connection.Response response = Jsoup.connect(imgUrl).ignoreContentType(true).timeout(60000).execute();
            byte[] bytes = response.bodyAsBytes();
            fos = new FileOutputStream(new File("/Users/donghao/img/pic" + counter.incrementAndGet() + ".jpg"));
            fos.write(bytes);
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
            System.out.println("img src:" + src);
            imgUrlQueue.add(src);
        }

    }
    
    private static void downloadPicture() {
        String imgUrl = imgUrlQueue.push();
        if (!Strings.isNullOrEmpty(imgUrl)) {
            System.out.println("start downloading ..." + " url is:" + imgUrl);
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
