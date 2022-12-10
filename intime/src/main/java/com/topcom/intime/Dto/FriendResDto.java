package com.topcom.intime.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendResDto {
	private int id;
    private String username;
    private String deviceToken;
    private Integer lateCount;
}
