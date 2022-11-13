package com.topcom.intime.repository;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcom.intime.model.SchedulePool;

public interface SchedulePoolRepository extends JpaRepository<SchedulePool, Integer>{
	
    Optional<SchedulePool> findById(String id); //query method
    
    long deleteById(String id);
	
	@Modifying
	@Query(value="INSERT INTO SchedulePool(id,time,destName) VALUES(?1,?2,?3)", nativeQuery = true)
	public int mSave(String id, Timestamp time, String destName);

	@Modifying
	@Query(value="INSERT INTO SchedulePoolMembers(SchedulePoolId,userId) VALUES(?1,?2)", nativeQuery = true)
	public int mAddMember(String pid, int uid);

}
