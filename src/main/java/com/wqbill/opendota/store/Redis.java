package com.wqbill.opendota.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Redis {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void put(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void put(Object key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void set(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(Object key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }
}
