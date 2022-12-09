package com.topcom.intime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topcom.intime.model.AdBanner;

public interface AdBannerRepository extends JpaRepository<AdBanner, Integer> {

	List<AdBanner> findAllByadvertiserId(int aid);
}
