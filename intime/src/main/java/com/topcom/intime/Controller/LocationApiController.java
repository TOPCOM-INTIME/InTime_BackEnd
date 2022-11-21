package com.topcom.intime.Controller;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.Dto.LocationsDto;
import com.topcom.intime.service.LocationService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class LocationApiController {

    private LocationService locationService;
    public LocationApiController(LocationService locationService){
        this.locationService=locationService;
    }

    @ApiOperation(value="Save a recent location of individual")
    @PostMapping("/{useridx}/location")
    public LocationDto getUserLocation(@PathVariable int useridx, @RequestBody LocationDto locationDto){
        return locationService.post(useridx, locationDto);
    }

    @ApiOperation(value="Get location information of individual user")
    @GetMapping("/{useridx}/location")
    public Object findByUserIdx(@PathVariable int useridx){
        return locationService.findByUserIdx(useridx);
    }

    //단체 일정 Test 구간//
    @ApiOperation(value="Save recent locations of individuals included in the same schedule")
    @PostMapping("/{scheduleIdx}/{useridx}/location")
    public LocationsDto shareLocation(@PathVariable String scheduleIdx, @PathVariable int useridx){
        return locationService.shareLocations(scheduleIdx, useridx);
    }

    @ApiOperation(value="Get users' locations from the schedule")
    @GetMapping("/{scheduleIdx}/locations")
    public Set<Object> getUsersLocation(@PathVariable String scheduleIdx){
        return locationService.getUsersLocations(scheduleIdx);
    }
}
