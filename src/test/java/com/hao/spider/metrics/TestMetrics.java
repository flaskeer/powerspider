package com.hao.spider.metrics;

import com.codahale.metrics.*;
import com.hao.spider.queue.Queue;
import com.netflix.servo.jsr166e.*;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by donghao on 16/8/15.
 */
public class TestMetrics {

    public static final MetricRegistry metrics = new MetricRegistry();


    @Test
    public void test() {
        List<String> q = new LinkedList<String>();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .convertRatesTo(TimeUnit.SECONDS)
                .build();
        reporter.start(1,TimeUnit.SECONDS);
        metrics.register(MetricRegistry.name(TestMetrics.class, "queue", "size"), new Gauge<Integer>() {

            @Override
            public Integer getValue() {
                return q.size();
            }
        });
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            q.add("job-xxx");
        }
    }

    @Test
    public void testCount() throws InterruptedException {
        LinkedBlockingQueue<String> q = new LinkedBlockingQueue<>();
        ThreadLocalRandom  random = ThreadLocalRandom.current();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
        reporter.start(1,TimeUnit.SECONDS);
        Counter  pendingJobs = metrics.counter(MetricRegistry.name(List.class,"pending-job","size"));
        int num = 1;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (random.nextDouble() > 0.7) {
                pendingJobs.dec();
                String job = q.take();
                System.out.println("take job--" + job);
            } else {
                String job = "num--" + num;
                pendingJobs.inc();
                q.offer(job);
                System.out.println("add job :" + job);
            }
            num++;
        }


    }

    @Test
    public void testMeter() {
        Random random = new Random();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
        reporter.start(1,TimeUnit.SECONDS);
        Meter meter = metrics.meter(MetricRegistry.name(TestMetrics.class,"request","tps"));
        int num = 5;
        while (true) {
            while (random.nextInt(num) > 0) {
                System.out.println("request");
                meter.mark();
                num--;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    public void testTimer() throws InterruptedException {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
        reporter.start(1,TimeUnit.SECONDS);
        Timer timer = metrics.timer(MetricRegistry.name(TestMetrics.class,"get-latency"));
        Timer.Context ctx;
        while (true) {
            ctx = timer.time();
            Thread.sleep(1000);
            ctx.stop();
        }
    }

    @Test
    public void testTime() {
        LocalTime time = LocalTime.now();
        time = time.with(ChronoField.MINUTE_OF_DAY, 59).with(ChronoField.HOUR_OF_DAY, 23).with(ChronoField.MILLI_OF_DAY, 59);
        System.out.println(time);
    }
}
