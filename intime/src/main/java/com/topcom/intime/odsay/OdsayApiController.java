package com.topcom.intime.odsay;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.odsay.dto.OdsayResponseDto;

@RestController
public class OdsayApiController {

	@Autowired
	OdsayService odsayService;
	
	@PostMapping("/odsay")
	public OdsayResponseDto RequestOdsayApi() {
		try {
			return odsayService.requestApi();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
