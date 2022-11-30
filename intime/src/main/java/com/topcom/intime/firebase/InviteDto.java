package com.topcom.intime.firebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteDto {

	private String targetToken;
	private String scheduleName;
	private String scheduleTime;
	private String destName;
	
}
