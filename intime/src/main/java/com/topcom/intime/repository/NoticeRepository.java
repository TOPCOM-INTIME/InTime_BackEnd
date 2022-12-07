package com.topcom.intime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topcom.intime.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

}
