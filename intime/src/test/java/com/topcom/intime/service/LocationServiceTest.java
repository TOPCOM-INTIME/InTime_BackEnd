package com.topcom.intime.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LocationServiceTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void post(){
        ValueOperations<String, Object> valueOperations=redisTemplate.opsForValue();
        String key="userIdx";
        valueOperations.set(key, "37");
        Object value=valueOperations.get(key);
        assertThat(value).isEqualTo("37");
    }

}