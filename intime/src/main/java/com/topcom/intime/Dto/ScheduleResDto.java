package com.topcom.intime.Dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	private String sourceX;
	private String srouceY;
	private String destName;
	private String destX;
	private String destY;
	private SchedulePool schedulePool;
	private String status;
	
}
