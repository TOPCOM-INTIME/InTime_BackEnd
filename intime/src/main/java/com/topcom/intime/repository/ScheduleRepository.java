package com.topcom.intime.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcom.intime.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{
	
	@Modifying
	@Query(value = "SELECT * FROM Schedule WHERE userId = :uid", nativeQuery = true)
	public List<Schedule> findAllByUid(@Param("uid")int uid);

	@Modifying
	@Query(value = "SELECT * FROM Schedule s WHERE userId = :uid ORDER BY s.id desc LIMIT 1", nativeQuery = true)
	public List<Schedule> findLatestCreatedSchedule(@Param("uid")int uid);
	
	@Modifying
	@Query(value = "UPDATE Schedule s SET s.SchedulePoolId = :poolId WHERE s.id = :scheduleId", 
	nativeQuery = true)
	public int mUpdatePoolId(@Param("poolId") String pid, @Param("scheduleId") int sid);
	
	@Modifying
	@Query(value = "UPDATE Schedule s SET s.status = :status WHERE s.id = :scheduleId", 
	nativeQuery = true)
	public int mUpdateStatus(@Param("status") String status, @Param("scheduleId") int sid);
	
	
}
