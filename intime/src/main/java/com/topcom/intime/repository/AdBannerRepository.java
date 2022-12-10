package com.topcom.intime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.topcom.intime.model.AdBanner;

public interface AdBannerRepository extends JpaRepository<AdBanner, Integer> {

	List<AdBanner> findAllByadvertiserId(int aid);
	
	@Query(value = "SELECT * FROM AdBanner ORDER BY RAND() LIMIT 1", nativeQuery = true)
	public AdBanner findRandomBanner();
	
}
