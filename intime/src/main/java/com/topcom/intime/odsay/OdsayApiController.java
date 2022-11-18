package com.topcom.intime.odsay;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.odsay.dto.OdsayGpsDto;
import com.topcom.intime.odsay.dto.OdsayResponseDto;

@RestController
public class OdsayApiController {

	@Autowired
	OdsayService odsayService;
	
	@PostMapping("api/odsay")
	public OdsayResponseDto RequestOdsayApi(@RequestBody OdsayGpsDto gpsDto) {
		System.out.println("TAGG:" + gpsDto);
		try {
			return odsayService.requestApi(gpsDto.getSx(), gpsDto.getSy(), gpsDto.getEx(), gpsDto.getEy());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
