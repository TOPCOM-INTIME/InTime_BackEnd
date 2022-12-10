package com.topcom.intime.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AdBannerResDto {
	
	private Integer id;
	private String fileUrl;
	private String filename;
	private String url;
	private Integer impression;
}
