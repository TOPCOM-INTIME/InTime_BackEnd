package com.topcom.intime.Dto;


import java.sql.Timestamp;

import javax.persistence.Lob;

import com.topcom.intime.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumResDto {
	
	private int id;
	private String title;
	@Lob
	private String content;
	private Timestamp createDate;
	private User writer;
}
