package com.topcom.intime.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadyPatternGroupResDto {

	private int id;
	private String name;
	private List<ReadyPatternResDto> patterns;

}
