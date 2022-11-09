package com.topcom.intime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.topcom.intime.model.ReadyPattern;

public interface ReadyPatternRepository extends JpaRepository<ReadyPattern, Integer>{

	@Modifying
	@Query(value="INSERT INTO ReadyPattern(name, time, userId, ReadyPatternGroupId, orderInGroup) VALUES(?1,?2,?3,?4,?5)", nativeQuery = true)
	public int mSave(String name, int time, int userId, Integer groupId, Integer orderInGroup);
	
	@Modifying
	@Query(value = "UPDATE ReadyPattern rp SET rp.ReadyPatternGroupId = :groupId, rp.orderInGroup = :order WHERE rp.id = :patternId", nativeQuery = true)
	public int mUpdateGroupId(@Param("patternId") int pid, @Param("order") Integer order, @Param("groupId") int gid);
	
	@Modifying
	@Query(value = "SELECT * FROM ReadyPattern WHERE userId = :uid AND ReadyPatternGroupId IS NULL", nativeQuery = true)
	public List<ReadyPattern> findAllByUid(@Param("uid")int uid);
	
	//@Query("update User u set u.status = :status where u.name = :name")
//	int updateUserSetStatusForName(@Param("status") Integer status, 
//			  @Param("name") String name);
}
