package com.hao.spider.queue;

import com.hao.spider.config.ConfigLoader;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jedis is not thread safe   but jedisPool is thread safe
 * Created by donghao on 16/7/23.
 */
@Slf4j
public class RedisQueue<T> implements Queue<T>{


    private JedisPool pool;

    public RedisQueue() {
        String host = ConfigLoader.getString("redis.host","127.0.0.1");
        int port = ConfigLoader.getInteger("redis.port",6379);
        log.info("now redis host is {},port is {}",host,port);
        pool = jedisPool(host,port);
    }

    @Override
    public void add(T body) {
        Jedis jedis = pool.getResource();
        try {
            jedis.lpush("imgUrl", (String) body);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public T push() {
        Jedis jedis = pool.getResource();
        try {
            T body = (T) jedis.rpop("imgUrl");
            if (body != null) {
                return body;
            }
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }



    private JedisPool jedisPool(String host,int port) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(ConfigLoader.getInteger("redis.maxIdle",200));
        config.setMaxTotal(ConfigLoader.getInteger("redis.maxTotal",30));
        config.setMaxWaitMillis(ConfigLoader.getLong("redis.waitMillis",10000));
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return new JedisPool(config,host,port);
    }
}
