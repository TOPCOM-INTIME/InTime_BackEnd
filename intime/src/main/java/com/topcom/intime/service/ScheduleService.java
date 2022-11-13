package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.ScheduleReqDto;
import com.topcom.intime.Dto.ScheduleResDto;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.repository.ScheduleRepository;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Transactional
	public void save_schedule(int uid, ScheduleReqDto schedule) {
		System.out.println("TAG2 : " + schedule);

		scheduleRepository.mSave(schedule.getName(), schedule.getTime(), uid
				, schedule.getSourceName(), schedule.getDestName());
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
					.schedulePool(s.getSchedulePool())
					.status(s.getStatus())
					.build();//x,y사용여부 확인 후 생성자로 변경

			scheduleResDtoList.add(scheduleResDto);
		
		}
		return scheduleResDtoList;
	}
	
	@Transactional
	public void update_ScheduleById(int id, ScheduleReqDto scheduleDto) {
		
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