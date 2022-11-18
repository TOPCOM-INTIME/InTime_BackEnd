package com.topcom.intime.Dto.Schedule;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.topcom.intime.Dto.ReadyPattern.SaveOnePatternDto;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.model.SchedulePool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResDto {

	private int id;
	private String name;
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp time;
	private String sourceName;
	private String destName;
	private String schedulePoolId;
	private String status;
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp startTime;
	
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp readyTime;
	
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp endTime;
	
	private List<SaveOnePatternDto> patterns;
	
}
