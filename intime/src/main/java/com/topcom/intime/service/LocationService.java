package com.topcom.intime.service;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.utils.KeyGen;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserRepository userRepository;


    public LocationDto post(int useridx, LocationDto locationDto){
        if(!userRepository.existsById(useridx)){
            throw new ResourceNotFoundException("User", "useridx", useridx);
        }
        String key=KeyGen.KeyGenerated(useridx);
        LocationDto newLocation= new LocationDto();
        newLocation.setGps_x(locationDto.getGps_x());
        newLocation.setGps_y(locationDto.getGps_y());
        redisTemplate.opsForValue().set(key, newLocation);
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        return newLocation;
    }

    public LocationDto findByUserIdx(int useridx){
        String key= KeyGen.KeyGenerated(useridx);
        return (LocationDto) redisTemplate.opsForValue().get(key);
    }

}

