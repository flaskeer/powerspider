package com.hao.spider.core;

import com.hao.spider.Model.Message;
import com.hao.spider.module.Module;
import com.hao.spider.module.ModuleType;
import com.hao.spider.module.Moduler;
import com.hao.spider.module.Notify;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by donghao on 16/8/20.
 */
public abstract class AbstractFactory<REQ,RES> implements Module<REQ,RES>,Notify{

    protected Moduler<REQ,RES> moduler = new Moduler<>();

    protected Map<ModuleType,Module<REQ,RES>> modules = new LinkedHashMap<>();


    @Override
    public void start() throws Throwable {

    }

    @Override
    public void notify(Moduler<REQ, RES> module) {

    }


    @Override
    public void notify(Message message) {
        this.notify(moduler);
    }

    @Override
    public void destory() throws Throwable {

    }
}
