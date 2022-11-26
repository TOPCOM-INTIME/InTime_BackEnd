package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.ReadyPattern.PatternResDto;
import com.topcom.intime.Dto.ReadyPattern.SaveOnePatternDto;
import com.topcom.intime.Dto.ReadyPattern.UpdateOrderDto;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.repository.ReadyPatternRepository;

@Service
public class ReadyPatternService {
	
	@Autowired
	private ReadyPatternRepository readyPatternRepository;

	@Transactional
	public void save_pattern(int uid, SaveOnePatternDto patternDto) {
		
		ReadyPattern newPattern = ReadyPattern.builder()
				.name(patternDto.getName())
				.time(patternDto.getTime())
				.userId(uid)
				.build();
		readyPatternRepository.save(newPattern);
	}
	
	@Transactional
	public void save_pattern_in_schedule(List<Integer> patternIds, Schedule schedule) {
		
		int i =1;
		for (int patternId : patternIds) {
			ReadyPattern origin_pattern = readyPatternRepository.findById(patternId)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPattern by id : " + patternId);
				});
			ReadyPattern cloned_pattern = ReadyPattern.builder()
					.name(origin_pattern.getName())
					.time(origin_pattern.getTime())
					.schedule(schedule)
					.orderInSchedule(i)
					.build();
			readyPatternRepository.save(cloned_pattern);
			i++;
		}
	}
	
	@Transactional
	public void updateNameOrTime(int id, SaveOnePatternDto readyPatternReqDto) {

		ReadyPattern readyPattern = readyPatternRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPatten by id: " + id);
				});
		readyPattern.setName(readyPatternReqDto.getName());
		readyPattern.setTime(readyPatternReqDto.getTime());
	}
	
	@Transactional
	public void UpdateGroupIdOfPatterns(int uid, int gid, List<UpdateOrderDto> patternList) {
		
		for (UpdateOrderDto pattern : patternList) {
			readyPatternRepository.mUpdateGroupIdOfPatterns(uid, pattern.getId(), pattern.getOrderInGroup(), gid);
		}
	}
	
	@Transactional
	public List<PatternResDto> findOriginsByUid(int uid) {

		List<ReadyPattern> patterns = readyPatternRepository.findOriginsByUid(uid);
		List<PatternResDto> dtoList = new ArrayList<>();
		for (ReadyPattern rp: patterns) {
			dtoList.add(new PatternResDto(rp.getId(), rp.getName(), rp.getTime(), 			rp.getUserId(), rp.getIsInGroup(), rp.getOrderInSchedule()));
		}
		return dtoList;
	}
	
	@Transactional
	public void deletePatternById(int pid) {
		readyPatternRepository.deleteById(pid);
	}
	
	
}
