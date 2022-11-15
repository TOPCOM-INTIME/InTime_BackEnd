package com.topcom.intime.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.topcom.intime.model.SchedulePoolMembers;


public interface SchedulePoolMembersRepository extends JpaRepository<SchedulePoolMembers, Integer>{
    
   List<SchedulePoolMembers> findAllByschedulePoolId(String poolId);

	@Modifying
	@Query(value="INSERT INTO SchedulePoolMembers(SchedulePoolId,userId) VALUES(?1,?2)", nativeQuery = true)
	public int mAddMember(String pid, int uid);
	
}
