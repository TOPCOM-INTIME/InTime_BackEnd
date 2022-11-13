package com.topcom.intime.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topcom.intime.model.SchedulePoolMembers;


public interface SchedulePoolMembersRepository extends JpaRepository<SchedulePoolMembers, Integer>{
    
   List<SchedulePoolMembers> findAllByschedulePoolId(String poolId);
}
