package com.zhaihuilin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaihuilin on 2017/11/15  13:22.
 */
@Component
public class JedisUtils {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String,String> valueOperations;

    public  String  get(String key){
        String value= valueOperations.get(key);
        if (StringUtils.isNotEmpty(value)){
            return  value;
        }
        return null;
    }

    public void  set(String key,String value){
        valueOperations.set(key,value);
    }

    public  void  set(String key,String value,long outTime){
        valueOperations.set(key,value,outTime, TimeUnit.SECONDS);
    }



































}
