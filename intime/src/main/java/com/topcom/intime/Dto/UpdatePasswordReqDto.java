package com.topcom.intime.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordReqDto {
    private String newPwd;
}
