package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.UserResDto;
import com.topcom.intime.Dto.Schedule.ScheduleInvitationDto;
import com.topcom.intime.Dto.Schedule.ScheduleResDto;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.model.SchedulePool;
import com.topcom.intime.model.SchedulePoolMembers;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.SchedulePoolMembersRepository;
import com.topcom.intime.repository.SchedulePoolRepository;

@Service
public class SchedulePoolService {

	@Autowired
	private SchedulePoolRepository schedulePoolRepository;
	
	@Autowired
	private SchedulePoolMembersRepository membersRepository;
	
	@Transactional
	public int save_pool(Schedule schedule, User user) {
		List<Schedule> scheduleList = new ArrayList<>();
		scheduleList.add(schedule);
		
		SchedulePool schedulePool = SchedulePool.builder()
				.schedules(scheduleList)
				.leader(user)
				.build();
		return schedulePoolRepository.save(schedulePool).getId();
	}
	
	@Transactional
	public List<ScheduleResDto> findSchedulesInPoolById(int pid) {

		SchedulePool schedulePool = schedulePoolRepository.findById(pid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find SchedulePool by id: " + pid);
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
	public List<UserResDto> findMembersInPoolByPid(int pid) {
		List<SchedulePoolMembers> schedulePoolMembersList =  membersRepository.findAllByschedulePoolId(pid);
		List<UserResDto> userDtoList = new ArrayList<>();
		for (SchedulePoolMembers member : schedulePoolMembersList) {
			UserResDto userDto = new UserResDto(member.getUser().getId(), member.getUser().getUsername(), member.getUser().getEmail());
			userDtoList.add(userDto);
		}
		return userDtoList;
	}
	
	@Transactional
	public List<ScheduleInvitationDto> findScheduleInvitations(int uid) {
		List<Integer> poolIds = membersRepository.mFindSchedulePoolInvited("INVITING", uid);
		System.out.println("TAGGGGG : " + poolIds);
		List<SchedulePool> pool_List = schedulePoolRepository.findAllById(poolIds);
		System.out.println("TAGGGGG2222 : " + pool_List);
		List<ScheduleInvitationDto> invitationDtoList = new ArrayList<>();
		for (SchedulePool pool : pool_List) {
			invitationDtoList.add(
					ScheduleInvitationDto.builder()
					.schedulePoolId(pool.getId())
					.invitorName(pool.getLeader().getUsername())
					.scheduleName(pool.getSchedules().get(0).getName())
					.destName(pool.getSchedules().get(0).getDestName())
					.endTime(pool.getSchedules().get(0).getEndTime())
					.build()
			);
		}
		return invitationDtoList;
	}
	
	@Transactional
	public void AddMemberInSchedule(int pid, int uid, boolean is_leader) {
		
		if (is_leader == true) {
			membersRepository.mAddLeader(pid, uid, "LEADER");
		}
		else {
			membersRepository.mAddMember(pid, uid);
		}
	}
	
	@Transactional
	public void AcceptScheduleInvite(int pid, int uid) {
		String status = "OK";
		membersRepository.mUpdateStatus(status, uid, pid);
	}
	
	@Transactional
	public void delete_schedulePoolById(String id) {
		
		schedulePoolRepository.deleteById(id);
	}
	
	
}
