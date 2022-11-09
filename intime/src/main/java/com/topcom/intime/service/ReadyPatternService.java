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
	public void create(ReadyPatternReqDto readyPatternReqDto) {
		System.out.println("TAG2 : " + readyPatternReqDto);

		readyPatternRepository.mSave(readyPatternReqDto.getName(), readyPatternReqDto.getTime(), 
				readyPatternReqDto.getUserId(), readyPatternReqDto.getGroupId(), readyPatternReqDto.getOrderInGroup());

	}
	
	@Transactional
	public void create2(List<ReadyPatternReqDto> patternReqDtoList) {
		for(ReadyPatternReqDto dto : patternReqDtoList) {
			readyPatternRepository.mSave(dto.getName(), dto.getTime(), 
					dto.getUserId(), dto.getGroupId(), dto.getOrderInGroup());
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
	public void updateGroupId(int gid, List<ReadyPatternReqDto> patternDtoList) {
		
		for (ReadyPatternReqDto dto : patternDtoList) {
			readyPatternRepository.mUpdateGroupId(dto.getId(), dto.getOrderInGroup(), gid);
		}
	}
	
	@Transactional
	public List<ReadyPatternResDto> findAllByUid(int uid) {

		List<ReadyPattern> patterns = readyPatternRepository.findAllByUid(uid);
		List<ReadyPatternResDto> dtoList = new ArrayList<>();
		for (ReadyPattern rp: patterns) {
			if (rp.getReadyPatternGroup() != null) {
				dtoList.add(new ReadyPatternResDto(rp.getId(), rp.getName(), rp.getTime(), rp.getUser().getId(), rp.getReadyPatternGroup().getId(), rp.getOrderInGroup()));
			}
			else {
				dtoList.add(new ReadyPatternResDto(rp.getId(), rp.getName(), rp.getTime(), rp.getUser().getId(), null, null));
			}
		}
		
		System.out.println("TAG : " + dtoList);
		return dtoList;
	}
	
	
}
