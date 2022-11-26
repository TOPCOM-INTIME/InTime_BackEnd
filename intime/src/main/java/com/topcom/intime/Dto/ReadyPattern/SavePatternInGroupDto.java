package com.topcom.intime.Dto.ReadyPattern;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavePatternInGroupDto {

	private String name;
	
	private List<Integer> patterns_Ids;
	
}
