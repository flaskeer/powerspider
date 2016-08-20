package com.hao.spider.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by donghao on 16/7/18.
 */
public class TestFetch {


    @Test
    public void testHttp() throws IOException {
        Connection.Response body = Jsoup.connect("http://monitor.ziroom.com/cat/r/t?domain=ams&date=2016081214&ip=All&type=URL&sort=tail&forceDownload=xml").execute();
        FileOutputStream outputStream = new FileOutputStream(new File("/Users/donghao/ziroom/ss.xml"));
        outputStream.write(body.bodyAsBytes());
    }


    @Test
    public void testRandom() {
        long time = new Date().getTime();
        System.out.println(time);
    }

}
