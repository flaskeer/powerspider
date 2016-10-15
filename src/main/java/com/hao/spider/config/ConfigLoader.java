package com.hao.spider.config;

import com.netflix.config.*;

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

    public static Boolean getBoolean(String key,Boolean defaultValue) {
        DynamicBooleanProperty booleanProperty = DynamicPropertyFactory.getInstance().getBooleanProperty(key,defaultValue);
        return defaultValue;
    }
}
