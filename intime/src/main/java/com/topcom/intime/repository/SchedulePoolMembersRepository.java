package com.topcom.intime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.topcom.intime.model.SchedulePoolMembers;


public interface SchedulePoolMembersRepository extends JpaRepository<SchedulePoolMembers, Integer>{
    
   List<SchedulePoolMembers> findAllByschedulePoolId(int poolId);

   @Modifying
	@Query(value="INSERT INTO SchedulePoolMembers(SchedulePoolId,userId, status) VALUES(?1,?2,?3)", nativeQuery = true)
	public int mAddLeader(int pid, int uid, String status);
   
	@Modifying
	@Query(value="INSERT INTO SchedulePoolMembers(SchedulePoolId,userId) VALUES(?1,?2)", nativeQuery = true)
	public int mAddMember(int pid, int uid);
	
	@Modifying
	@Query(value="UPDATE SchedulePoolMembers m SET m.status = ?1 "
			+ "WHERE m.userId = ?2 AND m.schedulePoolId = ?3"
			, nativeQuery = true)
	public int mUpdateStatus(String status, int uid, int pid);
	
	@Modifying
	@Query(value="SELECT SchedulePoolId FROM SchedulePoolMembers m "
			+ "WHERE m.status = ?1 AND m.userId = ?2", nativeQuery = true)
	public List<Integer> mFindSchedulePoolInvited(String status, int uid);
}
