package com.topcom.intime.service;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.Dto.LocationsDto;
import com.topcom.intime.exception.APIException;
import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.SchedulePoolMembers;
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

    public LocationsDto shareLocations(String scheduleIdx, int useridx){
        List<SchedulePoolMembers> membersList=schedulePoolMembersRepository.findAllByschedulePoolId(scheduleIdx);
        for(SchedulePoolMembers member:membersList){
            if(useridx!= member.getId()){
                throw new APIException(HttpStatus.BAD_REQUEST, "단체 일정에 포함되지 않은 유저입니다.");
            }
        }
        SetOperations<String, Object> setOperations= redisTemplate.opsForSet();
        LocationsDto locationsDto=new LocationsDto();
        locationsDto.setUseridx(useridx);
        locationsDto.setGps_x(this.findByUserIdx(useridx).getGps_x());
        locationsDto.setGps_y(this.findByUserIdx(useridx).getGps_y());
        setOperations.add(scheduleIdx, locationsDto);
        redisTemplate.expire(scheduleIdx, 1, TimeUnit.MINUTES);
        return locationsDto;
    }

    public Set getUsersLocations(String scheduleIdx){
        if(!schedulePoolRepository.existsById(scheduleIdx)){
            throw new ResourceNotFoundException("Schedule", scheduleIdx, Long.parseLong(scheduleIdx));
        }
        return redisTemplate.opsForSet().members(scheduleIdx);
    }
}

