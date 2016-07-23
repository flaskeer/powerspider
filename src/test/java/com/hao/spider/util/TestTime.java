package com.hao.spider.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by donghao on 16/7/18.
 */
public class TestTime {

    private String showTime(String channel,String team,String project,long interval,long sendTime,long receiveTime) {
        String identity = channel + "  " + team + "  " + project + " " ;
        if (interval >= 0 && interval <= 60) {
            return identity + ":0-1Min sendTime:" + format(sendTime) + " receiveTime:" + format(receiveTime);
        } else if (interval > 60 && interval <= 180) {
            return identity + ":1-3Min sendTime:" + new Date(sendTime) + " endTime:" + new Date(receiveTime);
        } else if (interval > 180 && interval <= 300) {
            return identity + ":3-5Min sendTime:" + new Date(sendTime) + " endTime:" + new Date(receiveTime);
        } else if (interval > 300 && interval <= 600) {
            return identity + ":5-10Min sendTime:" + new Date(sendTime) + " endTime:" + new Date(receiveTime);
        }
        return identity + ":>10Min sendTime:" + new Date(sendTime) + " endTime:" + new Date(receiveTime);
    }

    @Test
    public void test() {
        String s = showTime("xx-ww","team", "project", 3, System.currentTimeMillis(), System.currentTimeMillis());
        System.out.println(s);
    }

    private String format(long time) {
        return DateFormatUtils.format(time,"yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testFormat() {
        String format = format(1468907425);
        System.out.println(format);
    }
}
