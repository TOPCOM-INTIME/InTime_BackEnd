package com.topcom.intime.Dto;


import java.sql.Timestamp;

import javax.persistence.Lob;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeResDto {
	
	private int id;
	private String title;
	@Lob
	private String content;
	private Timestamp createDate;
}
