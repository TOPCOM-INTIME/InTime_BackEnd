package com.topcom.intime.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocationDto implements Serializable {
    private String gps_x;
    private String gps_y;
}
