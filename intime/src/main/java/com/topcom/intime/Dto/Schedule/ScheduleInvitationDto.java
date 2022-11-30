package com.topcom.intime.Dto.Schedule;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleInvitationDto {

	private Integer schedulePoolId;
	private String invitorName;
	private String scheduleName;
	private String destName;
	@JsonFormat(timezone = "Asia/Seoul")
	private Timestamp time;	
}
