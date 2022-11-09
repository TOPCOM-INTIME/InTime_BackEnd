package com.topcom.intime.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadyPatternGroupReqDto {

	private String name;
	private int userId;

}
