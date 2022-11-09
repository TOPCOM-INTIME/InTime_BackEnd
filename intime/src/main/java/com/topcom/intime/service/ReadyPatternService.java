package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.ReadyPatternReqDto;
import com.topcom.intime.Dto.ReadyPatternResDto;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.repository.ReadyPatternRepository;

@Service
public class ReadyPatternService {
	
	@Autowired
	private ReadyPatternRepository readyPatternRepository;

	@Transactional
	public void save_pattern(int uid, ReadyPatternReqDto pattern) {

		readyPatternRepository.mSave(pattern.getName(), pattern.getTime(), 
				uid, pattern.getGroupId(), pattern.getOrderInGroup());

	}
	
	@Transactional
	public void save_patternList(int uid, int gid, List<ReadyPatternReqDto> patternList) {
		for(ReadyPatternReqDto pattern : patternList) {
			readyPatternRepository.mSave(pattern.getName(), pattern.getTime(), 
					uid, gid, pattern.getOrderInGroup());
		}
	}
	
	@Transactional
	public void updateNameOrTime(int id, ReadyPatternReqDto readyPatternReqDto) {

		ReadyPattern readyPattern = readyPatternRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPatten by id: " + id);
				});
		readyPattern.setName(readyPatternReqDto.getName());
		readyPattern.setTime(readyPatternReqDto.getTime());
	}
	
//	@Transactional
//	public void getInGroup(int pid, int gid, ReadyPatternReqDto readyPatternReqDto) {
//		
//		readyPatternRepository.mUpdateGroupId(pid, gid);
//	}
	
	@Transactional
	public void UpdateGroupIdOfPatterns(int uid, int gid, List<ReadyPatternReqDto> patternList) {
		
		for (ReadyPatternReqDto pattern : patternList) {
			readyPatternRepository.mUpdateGroupIdOfPatterns(uid, pattern.getId(), pattern.getOrderInGroup(), gid);
		}
	}
	
	@Transactional
	public List<ReadyPatternResDto> findAllByUid(int uid) {

		List<ReadyPattern> patterns = readyPatternRepository.findAllByUid(uid);
		List<ReadyPatternResDto> dtoList = new ArrayList<>();
		for (ReadyPattern rp: patterns) {
			dtoList.add(new ReadyPatternResDto(rp.getId(), rp.getName(), rp.getTime(), rp.getUser().getId(), null, null));
		}
		return dtoList;
	}
	
	@Transactional
	public List<ReadyPatternResDto> findOriginsByUid(int uid) {

		List<ReadyPattern> patterns = readyPatternRepository.findOriginsByUid(uid);
		List<ReadyPatternResDto> dtoList = new ArrayList<>();
		for (ReadyPattern rp: patterns) {
			dtoList.add(new ReadyPatternResDto(rp.getId(), rp.getName(), rp.getTime(), rp.getUser().getId(), null, null));
		}
		return dtoList;
	}
	
	@Transactional
	public void deletePatternById(int pid) {
		readyPatternRepository.deleteById(pid);
	}
	
	
}
