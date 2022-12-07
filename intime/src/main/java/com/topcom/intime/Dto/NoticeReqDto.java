package com.topcom.intime.Dto;

import javax.persistence.Lob;

import lombok.Data;

@Data
public class NoticeReqDto {
	
	private String title;
	@Lob
	private String content;
}
