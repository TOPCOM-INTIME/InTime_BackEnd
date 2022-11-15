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
public class UserResDto {

	private int id;
	private String email;
	private String username;

}
