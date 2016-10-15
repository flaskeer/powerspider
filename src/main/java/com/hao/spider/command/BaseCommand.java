package com.hao.spider.command;

import com.netflix.hystrix.HystrixCommand;

/**
 * @author dongh38@ziroom
 * @Date 16/10/15
 * @Time 上午10:29
 */
public abstract class BaseCommand<T> extends HystrixCommand<T>{

    protected BaseCommand(Setter setter) {
        super(setter);
    }



}
