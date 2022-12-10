package com.topcom.intime.Dto;

import javax.persistence.Lob;

import lombok.Data;

@Data
public class ForumReqDto {
	
	private String title;
	@Lob
	private String content;
}
