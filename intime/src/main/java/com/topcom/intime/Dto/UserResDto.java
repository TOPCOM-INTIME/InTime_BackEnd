package com.topcom.intime.Dto;

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
	private Integer lateCount;
}
