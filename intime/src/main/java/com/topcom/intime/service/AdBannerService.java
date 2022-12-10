package com.topcom.intime.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.topcom.intime.Dto.AdBannerResDto;
import com.topcom.intime.model.AdBanner;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.AdBannerRepository;

@Service
public class AdBannerService {
	
	@Autowired
	private AdBannerRepository adBannerRepository;

	@Value("${file.bannerUploadPath}")
	private String filePath;//함수 안에서만써야함.
	private String currentPath = System.getProperty("user.dir");
		
	@Transactional
	public AdBannerResDto getRandomBanner() {
		AdBanner randomBanner = adBannerRepository.findRandomBanner();

		int impression = randomBanner.getImpression();
		adBannerRepository.AddOnetoImpression(++impression, randomBanner.getId());
		AdBannerResDto adBannerResDto = new AdBannerResDto(
				randomBanner.getId(), randomBanner.getFilePath(),
				randomBanner.getFileName(), randomBanner.getUrl()
				,randomBanner.getImpression()
				);
		return adBannerResDto;
	}
	
	@Transactional
	public List<AdBannerResDto> getAllBannersByUid(int uid) {
		
		List<AdBanner> adBanners = adBannerRepository.findAllByadvertiserId(uid);
		List<AdBannerResDto> adBannerResDtos = new ArrayList<>();
		for (AdBanner adBanner : adBanners) {
			adBannerResDtos.add(new AdBannerResDto(adBanner.getId(),
					adBanner.getFilePath(), adBanner.getFileName()
					, adBanner.getUrl(), adBanner.getImpression()));
		}
		return adBannerResDtos;
	}
	
	@Transactional
	public int updateBannerById(int id, int uid, MultipartFile file, String url) {
		AdBanner findedAdBanner = adBannerRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find AdBanner By Id : " + id);
				});
		String filename = uid + "_" + file.getOriginalFilename();
		if (deleteFileInSpringBoot(filename)) {
			findedAdBanner.setFileName(file.getOriginalFilename());
			findedAdBanner.setFilePath("/AdBanners/" + "/" + file.getOriginalFilename());
			findedAdBanner.setUrl(url);
			try {
				boolean is_success = saveFileInSpringBoot(file, uid, filename);
				if (!is_success) {
					return 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 1;
		}
		return 0;
	}
	
	@Transactional
	public void deleteBannerById(int id) {
		AdBanner findedAdBanner = adBannerRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find AdBanner By Id : " + id);
				});
		String filename = findedAdBanner.getFileName();
		
		if (deleteFileInSpringBoot(filename)) {
			adBannerRepository.deleteById(id);
		}
	}
	
	@Transactional
	public int saveFile(MultipartFile file, String url, User user) {

		String filename = user.getId() + "_" + file.getOriginalFilename();
		String filepath = "/AdBanners/" + filename;
		System.out.println("FILENAME LOG : " + filename);
		System.out.println("FILEPATH LOG : " + filepath);
		try {
			boolean is_success = saveFileInSpringBoot(file, user.getId(), filename);
			if (!is_success) {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AdBanner adBanner = AdBanner.builder()
				.advertiser(user)
				.fileName(filename)
				.url(url)
				.impression(0)
				.filePath("/AdBanners/" + filename).build();

		int adBannerId = adBannerRepository.save(adBanner).getId();
		return adBannerId;
	}

	public boolean saveFileInSpringBoot(MultipartFile file, int uid, String filename) throws IllegalStateException, IOException {

		String savePath = currentPath + filePath;
		File savedFile = new File(savePath, filename);
		if (savedFile.exists()) {
			System.out.println(filename + " already exists");
			return false;
		}
		file.transferTo(savedFile);
		return true;
	}
	
	public boolean deleteFileInSpringBoot(String filename) {
		boolean is_success = false;
		File file = new File(currentPath + filePath + "/" +  filename);
		if(file.exists()) {
			if(file.delete()) {
				is_success = true;
			}
		}
		return is_success;
	}

	public boolean mkdir(int dirName) {
		File dir = new File(currentPath + filePath + "/" + dirName);
		boolean is_success = false;
		
		if (!dir.exists()) {
			try {
				dir.mkdir();
				System.out.println(currentPath + filePath + "/" + dirName + " is created.");
				is_success = true;
			} catch (Exception e) {
				e.getStackTrace();
			}
		} else {
			System.out.println(currentPath + filePath + "/" + dirName + " is already exists.");
			is_success = true;
		}
		return is_success;
	}

}
