package com.hao.spider.config;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

/**
 * Created by donghao on 16/7/23.
 */
public class ConfigLoader {



    public static String getString(String key,String defaultValue) {
        DynamicStringProperty stringProperty = DynamicPropertyFactory.getInstance().getStringProperty(key, defaultValue);
        return stringProperty.get();
    }

    public static Integer getInteger(String key,int defaultValue) {
        DynamicIntProperty intProperty = DynamicPropertyFactory.getInstance().getIntProperty(key,defaultValue);
        return intProperty.get();
    }

    public static Long getLong(String key,long defaultValue) {
        DynamicLongProperty longProperty = DynamicPropertyFactory.getInstance().getLongProperty(key, defaultValue);
        return longProperty.get();
    }
}
