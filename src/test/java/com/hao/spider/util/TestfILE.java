package com.hao.spider.util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;


/**
 * Created by donghao on 16/7/26.
 */
public class TestFile {

    Runtime runtime = Runtime.getRuntime();

    @Test
    public void test() {
        float ioUsage = 0.0f;
        try {
            Process process = runtime.exec("iostat");
           BufferedReader reader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (count++ >= 4) {
                    String[] temp = line.split("\\s+");
                    if (temp.length > 1) {
                        float util = Float.parseFloat(temp[temp.length - 1]);
                        ioUsage = (ioUsage>util)?ioUsage : util;
                    }
                }
            }
            if (ioUsage > 0) {
                System.out.println("本节点磁盘使用率为:" + ioUsage/100);
            }
            reader.close();
            process.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
