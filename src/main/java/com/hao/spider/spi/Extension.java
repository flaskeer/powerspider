package com.hao.spider.spi;

/**
 * Created by donghao on 16/8/20.
 */
public class Extension<T> {

    public static <T> T getExtensionLoader(Class<T> service,String name) {
        return ExtensionLoader.getExtensionLoader(service,name);
    }

    public static <T> ExtensionLoader<T> load(Class<T> service,ClassLoader loader) {
        return ExtensionLoader.load(service,loader);
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> service) {
        return ExtensionLoader.getExtensionLoader(service);
    }


}
