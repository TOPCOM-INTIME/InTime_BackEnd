package com.topcom.intime.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcom.intime.model.Schedule;
import com.topcom.intime.model.User;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{
	
	@Modifying
	@Query(value="INSERT INTO Schedule(name, time, userId, sourceName, destName, schedulePoolId) VALUES(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	public int mSave(String name, Timestamp time, int userId, String sourceName, String destName, String poolId);
	
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
	
}
