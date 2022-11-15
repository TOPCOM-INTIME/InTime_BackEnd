package com.topcom.intime.Dto.ReadyPattern;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatternGroupResDto {

	private int id;
	private String name;
	private List<PatternResDto> patterns;

}
