package com.hao.spider.module;

/**
 * Created by donghao on 16/8/20.
 */
public interface Module<REQ,RES> extends LifeCycle{

    void notify(Moduler<REQ,RES> module);

}
