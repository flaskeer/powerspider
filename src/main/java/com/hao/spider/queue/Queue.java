package com.hao.spider.queue;

/**
 * Created by donghao on 16/7/23.
 */
public interface Queue<T> {

    void add(T body);

    T push();



}
