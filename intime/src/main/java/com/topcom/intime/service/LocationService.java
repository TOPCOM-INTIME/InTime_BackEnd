package com.topcom.intime.service;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.Dto.LocationsDto;
import com.topcom.intime.exception.APIException;
import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.SchedulePoolMembers;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.SchedulePoolMembersRepository;
import com.topcom.intime.repository.SchedulePoolRepository;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.utils.KeyGen;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Autowired
    private SchedulePoolMembersRepository schedulePoolMembersRepository;

    @Autowired
    private SchedulePoolRepository schedulePoolRepository;

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

    public LocationsDto shareLocations(int scheduleIdx, int useridx){
        String strKey=KeyGen.StrKeyGenerated(scheduleIdx);
        User user=userRepository.findById(useridx).orElseThrow(()->new ResourceNotFoundException("User", "useridx", (long)useridx));
        SetOperations<String, Object> setOperations= redisTemplate.opsForSet();
        LocationsDto locationsDto=new LocationsDto();
        locationsDto.setUseridx(useridx);
        locationsDto.setUsername(user.getUsername());
        locationsDto.setGps_x(this.findByUserIdx(useridx).getGps_x());
        locationsDto.setGps_y(this.findByUserIdx(useridx).getGps_y());
        setOperations.add(strKey, locationsDto);
        redisTemplate.expire(strKey, 1, TimeUnit.MINUTES);
        return locationsDto;
    }

    public Set getUsersLocations(int scheduleIdx){
        String strKey=KeyGen.StrKeyGenerated(scheduleIdx);
        if(!schedulePoolRepository.existsById(scheduleIdx)){
            throw new ResourceNotFoundException("Schedule", "scheduleIdx", (long)scheduleIdx);
        }
        return redisTemplate.opsForSet().members(strKey);
    }
}

