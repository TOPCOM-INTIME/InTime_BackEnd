package com.topcom.intime.Dto.ReadyPattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatternResDto {

	int id;
	String name;
	int time;
	int userId;
	private String isInGroup;
	Integer orderInSchedule;
}
