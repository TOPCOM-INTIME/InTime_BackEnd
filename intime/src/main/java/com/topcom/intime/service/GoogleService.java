package com.topcom.intime.service;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.repository.UserRepository;
import com.topcom.intime.utils.KeyGen;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GoogleService {
    @Value("${app.google-apikey}")
    private String googlekey;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserRepository userRepository;

    public LocationDto getDatafromApi(int useridx, String address){
        if(!userRepository.existsById(useridx)){
            throw new ResourceNotFoundException("User", "useridx", useridx);
        }
        try{
            LocationDto locationDto=new LocationDto();
            String key= KeyGen.KeyGenerated(useridx);
            String surl="https://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(address, "UTF-8")+"&key="+googlekey;
            URL url= new URL(surl);
            InputStream is=url.openConnection().getInputStream();
            BufferedReader streamReader=new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder responseStrBuilder=new StringBuilder();
            String inputStr;
            while((inputStr=streamReader.readLine())!=null){
                responseStrBuilder.append(inputStr);
            }
            JSONObject jo=new JSONObject(responseStrBuilder.toString());
            JSONArray results=jo.getJSONArray("results");
            if(results.length()>0){
                JSONObject jsonObject;
                jsonObject=results.getJSONObject(0);
                Double lat=jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double lng=jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                locationDto.setGps_x(lat.toString());
                locationDto.setGps_y(lng.toString());
                redisTemplate.opsForValue().set(key, locationDto);
                redisTemplate.expire(key, 1, TimeUnit.MINUTES);
            }
            return locationDto;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public LocationDto findByUserIdx(int useridx){
        String key= KeyGen.KeyGenerated(useridx);
        return (LocationDto) redisTemplate.opsForValue().get(key);
    }

}

