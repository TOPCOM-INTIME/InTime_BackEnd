package com.topcom.intime.Dto.Schedule;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveScheduleDto {

	private String name;
	
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp time;
	
	private String sourceName;
	private String destName;
	
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp startTime;
	
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp readyTime;
	
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp endTime;
	
	private String status;
	
	private List<Integer> readyPatterns_Ids;
	
	private List<Integer> members_Ids;
}
