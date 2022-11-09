package com.topcom.intime.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadyPatternReqDto {

	private int id;
	private String name;
	private int time;
	private int userId;
	private Integer groupId;
	private Integer orderInGroup;
}
