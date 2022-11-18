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
import com.topcom.intime.model.SchedulePool;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.ReadyPatternRepository;
import com.topcom.intime.repository.SchedulePoolRepository;
//import com.topcom.intime.repository.SchedulePoolMembersRepository;
import com.topcom.intime.repository.ScheduleRepository;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;
//	@Autowired
//	private SchedulePoolMembersRepository membersRepository;
	@Autowired
	private SchedulePoolRepository schedulePoolRepository;
	@Autowired
	private ReadyPatternRepository readyPatternRepository;
	
	@Transactional
	public Schedule save_schedule(User user, SaveScheduleDto sDto, String poolId) {
		
		Schedule schedule = Schedule.builder()
				.user(user)
				.name(sDto.getName())
				.time(sDto.getTime())
				.sourceName(sDto.getSourceName())
				.destName(sDto.getDestName())
				.startTime(sDto.getStartTime())
				.readyTime(sDto.getReadyTime())
				.endTime(sDto.getEndTime())
				.readyPatterns_Ids(sDto.getReadyPatterns_Ids())
				.status(sDto.getStatus())
				.build();
		
		if (poolId != null) {
			System.out.println("TAGG2 : " );
			SchedulePool pool =  schedulePoolRepository.findById(poolId)
					.orElseThrow(()->{
						return new IllegalArgumentException("Failed to find SchedulePool by id: " + poolId);
					});
			System.out.println("TAGG3 : " );

			schedule.setSchedulePool(pool);
		}
		return scheduleRepository.save(schedule);
		

	}
	
	@Transactional
	public void update_schedulePoolId(int scheduleId, String poolId) {
		
		scheduleRepository.mUpdatePoolId(poolId, scheduleId);
	}
	
	@Transactional
	public List<ScheduleResDto> findSchedulesByUid(int uid) {
		List<ScheduleResDto> scheduleResDtoList = new ArrayList<>();
		
		for (Schedule s : scheduleRepository.findAllByUid(uid)) {
			//List<ReadyPattern> pattern_list = readyPatternRepository.findAllById(s.getReadyPatterns_Ids());
			//System.out.println("TAGGG1 : " + pattern_list);

			List<SaveOnePatternDto> patternDtoList = new ArrayList<>();
			for (Integer pid : s.getReadyPatterns_Ids()) {
				ReadyPattern rp = readyPatternRepository.findById(pid)
						.orElseThrow(()->{
							return new IllegalArgumentException("Failed to find ReadyPattern by id: " + pid);
						});
				patternDtoList.add(new SaveOnePatternDto(rp.getName(), rp.getTime()));
			}
			ScheduleResDto scheduleResDto = ScheduleResDto.builder()
					.id(s.getId()).name(s.getName())
					.time(s.getTime())
					.sourceName(s.getSourceName())
					.destName(s.getDestName())
					.status(s.getStatus())
					.patterns(patternDtoList)
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
		s.setStatus(scheduleDto.getStatus());
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
