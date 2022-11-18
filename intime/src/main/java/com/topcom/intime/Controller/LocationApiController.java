package com.topcom.intime.Controller;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class LocationApiController {

    private LocationService locationService;
    public LocationApiController(LocationService locationService){
        this.locationService=locationService;
    }

    @PostMapping("/{useridx}/location")
    public LocationDto getUserLocation(@PathVariable int useridx, @RequestBody LocationDto locationDto){
        return locationService.post(useridx, locationDto);
    }

    @GetMapping("/{useridx}/location")
    public Object findByUserIdx(@PathVariable int useridx){
        return locationService.findByUserIdx(useridx);
    }
}
