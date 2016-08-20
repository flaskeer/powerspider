package com.hao.spider.processor;

/**
 * Created by donghao on 16/8/20.
 */
public interface Processor<REQ,RES> {

    RES process(REQ req,Object ... args);
}
