package com.topcom.intime.repository;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.topcom.intime.model.SchedulePool;

public interface SchedulePoolRepository extends JpaRepository<SchedulePool, Integer>{
	
    Boolean existsById(String id);
    long deleteById(String id);
	
	@Modifying
	@Query(value="INSERT INTO SchedulePool(id,name,time,destName) VALUES(?1,?2,?3,?4)", nativeQuery = true)
	public int mSave(String id, String name, Timestamp time, String destName);


}
