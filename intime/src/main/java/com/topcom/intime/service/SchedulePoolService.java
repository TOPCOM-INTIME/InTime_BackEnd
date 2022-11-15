package com.topcom.intime.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.UserResDto;
import com.topcom.intime.Dto.Schedule.ScheduleResDto;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.model.SchedulePool;
import com.topcom.intime.model.SchedulePoolMembers;
import com.topcom.intime.repository.SchedulePoolMembersRepository;
import com.topcom.intime.repository.SchedulePoolRepository;

@Service
public class SchedulePoolService {

	@Autowired
	private SchedulePoolRepository schedulePoolRepository;
	
	@Autowired
	private SchedulePoolMembersRepository membersRepository;
	
	@Transactional
	public void save_pool(String poolId, String name, Timestamp time, String destName) {

		schedulePoolRepository.mSave(poolId, name, time, destName);
	}
	
	@Transactional
	public List<ScheduleResDto> findSchedulesInPoolById(String id) {

		SchedulePool schedulePool = schedulePoolRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find SchedulePool by id: " + id);
				});
		List<ScheduleResDto> dtoList = new ArrayList<>();
		for (Schedule s : schedulePool.getSchedules()) {
			dtoList.add(
					ScheduleResDto.builder()
					.id(s.getId())
					.name(s.getName())
					.destName(s.getDestName())
					.sourceName(s.getSourceName())
					.build()
					);
		}
		return dtoList;
	}
	
	@Transactional
	public List<UserResDto> findMembersInPoolByPid(String pid) {
		List<SchedulePoolMembers> schedulePoolMembersList =  membersRepository.findAllByschedulePoolId(pid);
		List<UserResDto> userDtoList = new ArrayList<>();
		for (SchedulePoolMembers member : schedulePoolMembersList) {
			UserResDto userDto = new UserResDto(member.getUser().getId(), member.getUser().getUsername(), member.getUser().getEmail());
			userDtoList.add(userDto);
		}
		return userDtoList;
	}
	
	@Transactional
	public void AddMemberInSchedule(String pid, int uid) {
		
		membersRepository.mAddMember(pid, uid);
	}
	
	@Transactional
	public void delete_schedulePoolById(String id) {
		
		schedulePoolRepository.deleteById(id);
	}
	
	
}
