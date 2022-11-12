package com.topcom.intime.Controller;

import com.topcom.intime.Dto.LocationDto;
import com.topcom.intime.service.GoogleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class GoogleApiController {
    private GoogleService googleService;
    public GoogleApiController(GoogleService googleService){
        this.googleService=googleService;
    }

    @GetMapping("/google/{useridx}")
    public LocationDto callGoogleApi(@PathVariable int useridx, @RequestParam String address){
        return googleService.getDatafromApi(useridx, address);
    }

    @GetMapping("/google/{useridx}/location")
    public Object findByUserIdx(@PathVariable int useridx){
        return googleService.findByUserIdx(useridx);
    }
}
