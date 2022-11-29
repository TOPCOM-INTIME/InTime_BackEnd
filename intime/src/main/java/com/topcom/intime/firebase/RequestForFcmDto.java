package com.topcom.intime.firebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestForFcmDto {

	private String targetToken;
	private String title;
	private String body;
	private String type;
	private String userName;
	private String scheduleName;
	private String scheduleTime;
	private String destName;
	
}
