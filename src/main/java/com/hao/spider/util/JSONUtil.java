package com.hao.spider.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 *
 * use jackson
 * @author dongh38@ziroom
 * @Date 16/9/16
 * @Time 上午11:59
 */
@Slf4j
public class JSONUtil {



    private ObjectMapper mapper;

    public JSONUtil(JsonInclude.Include include) {
        mapper = new ObjectMapper();
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static JSONUtil nonEmptyMapper() {
        return new JSONUtil(JsonInclude.Include.NON_EMPTY);
    }

    public static JSONUtil nonDefaultMapper() {
        return new JSONUtil(JsonInclude.Include.NON_DEFAULT);
    }

    public static JSONUtil getInstance() {
        return new JSONUtil(JsonInclude.Include.NON_DEFAULT).enableSimple();
    }

    public JSONUtil enableSimple() {
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
        return this;
    }

    /**
     * 私有化定制
     * @return
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("write to json string error:" + object,e);
            return null;
        }
    }

    public <T> T fromJson(String json,Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json,clazz);
        } catch (IOException e) {
            log.warn("parse json error:" + json,e);
            return null;
        }
    }

    /**
     *
     * @param json
     * @param javaType
     * @param <T>
     * @return
     */
    public <T> T fromJson(String json, JavaType javaType) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json,javaType);
        } catch (IOException e) {
            log.warn("parse json error:" + json,e);
            return null;
        }
    }
}
