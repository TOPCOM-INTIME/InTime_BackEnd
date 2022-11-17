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
public class SubPath {

//	private List<SubPathDetail> subPathDetails;
	private Integer trafficType;
//	private Integer distance;
	private Integer sectionTime;
	private Integer stationCount;
	private List<Lane> lane;
	private String startName;
	private String endName;
}
