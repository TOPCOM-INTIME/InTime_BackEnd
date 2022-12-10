package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.ReadyPattern.SaveOnePatternDto;
import com.topcom.intime.Dto.Schedule.SaveScheduleDto;
import com.topcom.intime.Dto.Schedule.ScheduleResDto;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.ReadyPatternRepository;
import com.topcom.intime.repository.SchedulePoolMembersRepository;
import com.topcom.intime.repository.SchedulePoolRepository;
import com.topcom.intime.repository.ScheduleRepository;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private SchedulePoolMembersRepository membersRepository;
	@Autowired
	private SchedulePoolRepository schedulePoolRepository;
	@Autowired
	private ReadyPatternRepository readyPatternRepository;
	
	@Transactional
	public Schedule save_schedule(User user, SaveScheduleDto sDto) {
		
		System.out.println("TAGG1 : " + sDto.getMembers_Ids());
		
		List<Integer>patternIds = sDto.getReadyPatterns_Ids();
		List<ReadyPattern> cloned_patternList = new ArrayList<>();
		int i =1;
		for (int patternId : patternIds) {
			ReadyPattern origin_pattern = readyPatternRepository.findById(patternId)
			.orElseThrow(()->{
				return new IllegalArgumentException("Failed to find ReadyPattern by id : " + patternId);
			});
			ReadyPattern cloned_pattern = ReadyPattern.builder()
					.name(origin_pattern.getName())
					.time(origin_pattern.getTime())
					.orderInSchedule(i)
					.build();
			cloned_patternList.add(cloned_pattern);
			i++;
		}
		
		Schedule schedule = Schedule.builder()
				.user(user)
				.name(sDto.getName())
				.time(sDto.getTime())
				.sourceName(sDto.getSourceName())
				.destName(sDto.getDestName())
				.startTime(sDto.getStartTime())
				.readyTime(sDto.getReadyTime())
				.endTime(sDto.getEndTime())
				.status(sDto.getStatus())
				.build();
		
		return scheduleRepository.save(schedule);
	}
	
	@Transactional
	public void update_scheduleStatus(String status, int scheduleId) {
		scheduleRepository.mUpdateStatus(status, scheduleId);
	}
	
	@Transactional
	public void update_schedulePoolId(int scheduleId, int poolId) {
		
		scheduleRepository.mUpdatePoolId(poolId, scheduleId);
	}
	
	@Transactional
	public List<ScheduleResDto> findSchedulesByUid(int uid) {
		List<ScheduleResDto> scheduleResDtoList = new ArrayList<>();
		
		for (Schedule s : scheduleRepository.findAllByUid(uid)) {

			List<SaveOnePatternDto> patternDtoList = new ArrayList<>();

			for (ReadyPattern pattern : s.getReadyPatterns()) {
				patternDtoList.add(new SaveOnePatternDto(pattern.getName(), pattern.getTime()));
			}
			ScheduleResDto scheduleResDto = ScheduleResDto.builder()
					.id(s.getId()).name(s.getName())
					.time(s.getTime())
					.sourceName(s.getSourceName())
					.destName(s.getDestName())
					.status(s.getStatus())
					.patterns(patternDtoList)
					.readyTime(s.getReadyTime())
					.startTime(s.getStartTime())
					.endTime(s.getEndTime())
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
		s.setReadyTime(scheduleDto.getReadyTime());
		s.setStartTime(scheduleDto.getStartTime());
		s.setEndTime(scheduleDto.getStartTime());
		s.setDestName(scheduleDto.getDestName());
		s.setSourceName(scheduleDto.getSourceName());
		s.setStatus(scheduleDto.getStatus());
	}
	
	@Transactional
	public void delete_scheduleById(int sid) {
		System.out.println("TAG11 : " + sid);

		Schedule finded_schedule = scheduleRepository.findById(sid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find Schedule By Id : " + sid);
				});
		System.out.println("TAGG1");
		System.out.println("TAG F : " + finded_schedule.getSchedulePool());

		if(finded_schedule.getSchedulePool() == null) {
			scheduleRepository.deleteById(sid);
		} else {
			int pid = finded_schedule.getSchedulePool().getId();
			System.out.println("TAGG2 : " + pid);
			membersRepository.deleteByschedulePoolId(pid);
			schedulePoolRepository.deleteById(pid);
		}
	}
	
	@Transactional
	public List<Schedule> findSchedulesByUid2(int uid) {
		
		return scheduleRepository.findAllByUid(uid);
	}
}
