package com.hao.spider.module;

/**
 * Created by donghao on 16/8/20.
 */
public interface LifeCycle {

    void start() throws Throwable;

    void destory() throws Throwable;
}
