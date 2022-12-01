package com.topcom.intime.service;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.Dto.LocationsDto;
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
    @Test //개인 위치 공유 test
    void LocationDtoTest(){
        ValueOperations<String, Object> valueOperations=redisTemplate.opsForValue();
        String key="userIdx";
        LocationDto locationDto= new LocationDto();
        locationDto.setGps_x("37");
        locationDto.setGps_y("122");
        valueOperations.set(key, locationDto);
        Object value=valueOperations.get(key);
        assertThat(value).isNotNull();
    }

    @Test //단체 일정 시에 위치 공유 test
    void LocationDtosTest(){
        SetOperations<String, Object> setOperations=redisTemplate.opsForSet();
        String key="scheduleIdx";
        LocationsDto locationsDto=new LocationsDto();
        locationsDto.setUseridx(1);
        locationsDto.setGps_x("37");
        locationsDto.setGps_y("122");
        setOperations.add(key, locationsDto);
        Set values=setOperations.members(key);
        assertThat(values).isNotNull();
    }
}