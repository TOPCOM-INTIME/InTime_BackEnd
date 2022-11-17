package com.topcom.intime.odsay.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
	
	private Integer searchType;
//	private Integer outTrafficCheck;
	private Integer busCount;
	private Integer subwayCount;
	private Integer subwayBusCount;
	private Integer pointDistance;
//	private Integer startRadius;
//	private Integer endRadius;
	private List<Path> path;
	
	
}
