package com.topcom.intime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.ReadyPatternGroup;

public interface ReadyPatternGroupRepository extends JpaRepository<ReadyPatternGroup, Integer> {
	
	@Modifying
	@Query(value="INSERT INTO ReadyPatternGroup (name, userId) VALUES(?1,?2)", nativeQuery = true)
	public int mSave(String name, int userId);
	
	@Modifying
	@Query(value = "SELECT * FROM ReadyPatternGroup WHERE userId = :uid", nativeQuery = true)
	public List<ReadyPatternGroup> findAllByUid(@Param("uid")int uid);
	
	@Modifying//TEST
	@Query(value = "SELECT ReadyPatterns FROM ReadyPatternGroup WHERE id = :id", nativeQuery = true)
	public List<ReadyPattern> test(@Param("id")int id);
}
