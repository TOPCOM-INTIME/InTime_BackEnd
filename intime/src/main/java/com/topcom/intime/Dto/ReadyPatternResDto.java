package com.topcom.intime.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadyPatternResDto {

	int id;
	String name;
	int time;
	int userId;
	Integer groupId;
	Integer orderInGroup;
}
