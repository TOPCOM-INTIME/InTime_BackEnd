package com.topcom.intime.service;

import java.io.File;
import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.topcom.intime.model.AdBanner;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.AdBannerRepository;

@Service
public class AdBannerService {

	@Autowired
	private AdBannerRepository adBannerRepository;

	private String filePath = System.getProperty("user.dir") + "/src/main/resources/static/image/AdBanners";
	@Transactional
	public int saveFile(MultipartFile file, User user) {

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
