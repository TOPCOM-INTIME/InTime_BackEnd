package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.Schedule.SaveScheduleDto;
import com.topcom.intime.Dto.Schedule.ScheduleResDto;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.repository.SchedulePoolMembersRepository;
import com.topcom.intime.repository.ScheduleRepository;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private SchedulePoolMembersRepository membersRepository;
	
	@Transactional
	public void save_schedule(int uid, SaveScheduleDto schedule, String poolId) {
		
		int i = scheduleRepository.mSave(schedule.getName(), schedule.getTime(), uid
				, schedule.getSourceName(), schedule.getDestName(), schedule.getStartTime()
				,schedule.getReadyTime(), schedule.getEndTime(), poolId);

		System.out.println("TAGG : " + i);

	}
	
	@Transactional
	public void save_GroupScheduleAfterInvited(int uid, SaveScheduleDto schedule, String poolId) {
		
		scheduleRepository.mSave(schedule.getName(), schedule.getTime(), uid
				, schedule.getSourceName(), schedule.getDestName(), schedule.getStartTime()
				,schedule.getReadyTime(), schedule.getEndTime(), poolId);
		membersRepository.mAddMember(poolId, uid);
		
	}
	
	@Transactional
	public void update_schedulePoolId(int scheduleId, String poolId) {
		
		scheduleRepository.mUpdatePoolId(poolId, scheduleId);
	}
	
	@Transactional
	public Schedule findLatestCreatedSchedule(int uid) {
		
		Schedule latestCreatedSchedule = scheduleRepository.findLatestCreatedSchedule(uid).get(0);
		
		return latestCreatedSchedule;
	}
	
	@Transactional
	public List<ScheduleResDto> findSchedulesByUid(int uid) {
		List<ScheduleResDto> scheduleResDtoList = new ArrayList<>();
		
		for (Schedule s : scheduleRepository.findAllByUid(uid)) {
			ScheduleResDto scheduleResDto = ScheduleResDto.builder()
					.id(s.getId()).name(s.getName())
					.time(s.getTime())
					.sourceName(s.getSourceName())
					.destName(s.getDestName())
					.status(s.getStatus())
					.build();//x,y사용여부 확인 후 생성자로 변경

			if (s.getSchedulePool() == null) {
				scheduleResDto.setSchedulePoolId(null);
			}
			else {
				scheduleResDto.setSchedulePoolId(s.getSchedulePool().getId());
			}
			
			scheduleResDtoList.add(scheduleResDto);
		
		}
		return scheduleResDtoList;
	}
	
	@Transactional
	public void update_ScheduleById(int id, SaveScheduleDto scheduleDto) {
		
		Schedule s = scheduleRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("Failed to find Schedule by id: " + id);
		});
		s.setName(scheduleDto.getName());
		s.setTime(scheduleDto.getTime());
		s.setDestName(scheduleDto.getDestName());
		s.setSourceName(scheduleDto.getSourceName());
	}
	
	@Transactional
	public void delete_scheduleById(int sid) {
		scheduleRepository.deleteById(sid);
	}
	
	@Transactional
	public List<Schedule> findSchedulesByUid2(int uid) {
		
		return scheduleRepository.findAllByUid(uid);
	}
}
