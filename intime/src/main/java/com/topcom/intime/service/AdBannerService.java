package com.topcom.intime.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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

	private String filePath = System.getProperty("user.dir") + "/src/main/resources/static/image/AdBanners";
	
	@Transactional
	public List<AdBannerResDto> getAllBannersByUid(int uid) {
		
		List<AdBanner> adBanners = adBannerRepository.findAllByadvertiserId(uid);
		List<AdBannerResDto> adBannerResDtos = new ArrayList<>();
		for (AdBanner adBanner : adBanners) {
			adBannerResDtos.add(new AdBannerResDto(adBanner.getId(), adBanner.getFilePath(), adBanner.getFileName()));
		}
		return adBannerResDtos;
	}
	
	@Transactional
	public int updateBannerById(int id, int uid, MultipartFile file) {
		AdBanner findedAdBanner = adBannerRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find AdBanner By Id : " + id);
				});
		if (deleteFileInSpringBoot(uid, findedAdBanner.getFileName())) {
			findedAdBanner.setFileName(file.getOriginalFilename());
			findedAdBanner.setFilePath("/image/AdBanners/" + uid + "/" + file.getOriginalFilename());
			try {
				boolean is_success = saveFileInSpringBoot(file, uid);
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

		adBannerRepository.deleteById(id);
	}
	
	@Transactional
	public int saveFile(MultipartFile file, User user) {
		System.out.println("TAG : FilePath : " + filePath);

		try {
			boolean is_success = saveFileInSpringBoot(file, user.getId());
			if (!is_success) {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AdBanner adBanner = AdBanner.builder()
				.advertiser(user)
				.fileName(file.getOriginalFilename())
				.filePath("/image/AdBanners/" + user.getId() + "/" + file.getOriginalFilename()).build();

		int adBannerId = adBannerRepository.save(adBanner).getId();
		return adBannerId;
	}

	public boolean saveFileInSpringBoot(MultipartFile file, int uid) throws Exception {

		if (mkdir(uid)) {
			String savePath = filePath + "/" + uid;
			String fileName = file.getOriginalFilename();
			File savedFile = new File(savePath, fileName);
			if (savedFile.exists()) {
				System.out.println(fileName + " already exists");
				return false;
			}
			file.transferTo(savedFile);
			return true;
		}

		throw new IOException("Failed to save file : " + file);
	}
	
	public boolean deleteFileInSpringBoot(int uid, String filename) {
		boolean is_success = false;
		File file = new File(filePath + "/" + uid + "/" + filename);
		if(file.exists()) {
			if(file.delete()) {
				is_success = true;
			}
		}
		return is_success;
	}

	public boolean mkdir(int dirName) {
		File dir = new File(filePath + "/" + dirName);
		boolean is_success = false;
		
		if (!dir.exists()) {
			try {
				dir.mkdir();
				System.out.println(filePath + "/" + dirName + " is created.");
				is_success = true;
			} catch (Exception e) {
				e.getStackTrace();
			}
		} else {
			System.out.println(filePath + "/" + dirName + " is already exists.");
			is_success = true;
		}
		return is_success;
	}

}
