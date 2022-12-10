package com.topcom.intime.WebController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.Dto.AdBannerResDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.User;
import com.topcom.intime.service.AdBannerService;

@RestController
public class AdvertiserApiController {

	@Autowired
	private AdBannerService adBannerService;

	@PostMapping("advertiser/adBanner")
	public ResponseDto<Integer> UploadAdBanner(@RequestParam("banner") MultipartFile banner, @RequestParam("url") String url) {

		int savedFileId = adBannerService.saveFile(banner, url, getPrincipal());
		if (savedFileId == 0) {
			return new ResponseDto<Integer>(HttpStatus.CONFLICT.value(), -1);

		}
		return new ResponseDto<Integer>(HttpStatus.OK.value(), savedFileId);
	}
	
	@GetMapping("api/randomBanner")
	public AdBannerResDto getRandomBanner() {
		return adBannerService.getRandomBanner();
	}
	
	@GetMapping(value = "advertiser/myAdBanner/all")
	public List<AdBannerResDto> getMyBanners() {
		
		return adBannerService.getAllBannersByUid(getPrincipal().getId());
	}
	
	@PutMapping("advertiser/adBanner={id}")
	public ResponseDto<Integer> changeBannerById(@PathVariable("id")int id, @RequestParam("banner") MultipartFile banner, @RequestParam("url") String url) {
		
		int is_success = adBannerService.updateBannerById(id, getPrincipal().getId(), banner, url);
		if (is_success == 1) {
			return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
		} else {
			return new ResponseDto<Integer>(HttpStatus.NOT_MODIFIED.value(), -1);
		}

	}
	
	@DeleteMapping("advertiser/adBanner={id}")
	public ResponseDto<Integer> deleteBannerById(@PathVariable("id")int id) {
		
		adBannerService.deleteBannerById(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
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
