package com.hao.spider.spi;

/**
 * Created by donghao on 16/8/20.
 */
@SPI
public interface ExtensionFactory {

    <T> T getExtension(Class<T> className,String name);

}
