package com.topcom.intime.odsay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OdsayResponseDto {

	private Result result;
	private Error error;
	
	@Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
	public static class Error {
		private String msg;
		private String code;
	}
}

