package com.topcom.intime.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocationsDto implements Serializable {
    private int useridx;
    private String username;
    private String gps_x;
    private String gps_y;
}
