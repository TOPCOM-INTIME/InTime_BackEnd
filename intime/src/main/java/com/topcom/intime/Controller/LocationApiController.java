package com.topcom.intime.Controller;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.Dto.LocationsDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.service.LocationService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PostMapping("/location")
    public LocationDto getUserLocation(@RequestBody LocationDto locationDto){
        Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalDetails principal = (PrincipalDetails)principalObject;

        return locationService.post(principal.getUser().getId(), locationDto);
    }

    @ApiOperation(value="Get location information of individual user")
    @GetMapping("/location")
    public Object findByUserIdx() {
        Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalDetails principal = (PrincipalDetails)principalObject;

        return locationService.findByUserIdx(principal.getUser().getId());

    }

    @ApiOperation(value="Save recent locations of individuals included in the same schedule")
    @PostMapping("/{scheduleIdx}/location")
    public LocationsDto shareLocation(@PathVariable int scheduleIdx){
        Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalDetails principal = (PrincipalDetails)principalObject;

        return locationService.shareLocations(scheduleIdx, principal.getUser().getId());
    }

    @ApiOperation(value="Get users' locations from the schedule")
    @GetMapping("/{scheduleIdx}/locations")
    public Set<Object> getUsersLocation(@PathVariable int scheduleIdx){
        return locationService.getUsersLocations(scheduleIdx);
    }
}
