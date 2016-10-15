package com.hao.spider.handler;

/**
 * Created by donghao on 16/8/20.
 */
public interface Handler<T> {

    void process(T req,Object ... args);
}
