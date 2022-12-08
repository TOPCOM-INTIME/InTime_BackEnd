package com.topcom.intime.WebController;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.User;
import com.topcom.intime.service.AdBannerService;

@RestController
public class AdvertiserApiController {

	@Autowired
	private AdBannerService adBannerService;

	/*getClass().getResourceAsStream의 기본 경로는 ../resources이다.*/
	private String filePath = "/static/image/AdBanners";

	@PostMapping("advertiser/adBanner")
	public ResponseDto<Integer> UploadAdBanner(@RequestParam("banner") MultipartFile banner) {

		System.out.println("Banner IMG : " + banner);
		int savedFileId = adBannerService.saveFile(banner, getPrincipal());
		if (savedFileId == 0) {
			return new ResponseDto<Integer>(HttpStatus.CONFLICT.value(), -1);

		}
		return new ResponseDto<Integer>(HttpStatus.OK.value(), savedFileId);
	}

//	@GetMapping(value = "advertiser/adBanner", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//	public @ResponseBody byte[] getFile() throws IOException {
//		System.out.println("FilePath : " + filePath + "/19/car.jpg");
//		InputStream in = getClass().getResourceAsStream(filePath + "/19/car.jpg");
//		return IOUtils.toByteArray(in);
//	}

	public User getPrincipal() {
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;
		return principal.getUser();
	}

}
