package com.hao.spider.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by donghao on 16/7/23.
 */
public class DefaultQueue<T> implements Queue<T>{

    private BlockingQueue<T> queue = new LinkedBlockingQueue<>(10000);

    @Override
    public void add(T body) {
        try {
            queue.put(body);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public T push() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }
}
