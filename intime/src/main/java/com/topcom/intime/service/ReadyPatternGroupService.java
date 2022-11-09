package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.ReadyPatternGroupReqDto;
import com.topcom.intime.Dto.ReadyPatternGroupResDto;
import com.topcom.intime.Dto.ReadyPatternResDto;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.ReadyPatternGroup;
import com.topcom.intime.repository.ReadyPatternGroupRepository;

@Service
public class ReadyPatternGroupService {
	
	@Autowired
	private ReadyPatternGroupRepository readyPatternGroupRepository;
	
	@Transactional
	public void save_group(int uid, ReadyPatternGroupReqDto groupReqDto) {
		readyPatternGroupRepository.mSave(groupReqDto.getName(), uid);

	}
	
	@Transactional
	public void updateGroupNameById(int id, ReadyPatternGroupReqDto groupReqDto) {

		ReadyPatternGroup patternGroup = readyPatternGroupRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPatten by id: " + id);
				});
		patternGroup.setName(groupReqDto.getName());
	}
	
	@Transactional
	public List<ReadyPatternResDto> findPatternsByGid(int uid, int gid) {
		ReadyPatternGroup patternGroup = readyPatternGroupRepository.findById(gid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPattenGroup by id: " + gid);
				});
		List<ReadyPatternResDto> dtoLIst = new ArrayList<>();
		for (ReadyPattern rp: patternGroup.getReadyPatterns()) {
			dtoLIst.add(new ReadyPatternResDto(rp.getId(), rp.getName(), rp.getTime(), rp.getUser().getId(), rp.getReadyPatternGroup().getId(), rp.getOrderInGroup()));
		}
//		System.out.println("TAG : " + dtoLIst);
		return dtoLIst;
	}
	
	@Transactional
	public List<ReadyPatternGroupResDto>  findAllGroupsByUid(int uid) {
		
		List<ReadyPatternGroup> groups = readyPatternGroupRepository.findAllByUid(uid);
		List<ReadyPatternGroupResDto> groupDtoList = new ArrayList<>();
		pComparator comp = new pComparator();
		for (ReadyPatternGroup group : groups) {
			List<ReadyPatternResDto> patternDtoList = new ArrayList<>();
			for(ReadyPattern rp : group.getReadyPatterns()) {
				patternDtoList.add(new ReadyPatternResDto(rp.getId(), rp.getName(), rp.getTime(), rp.getUser().getId(), rp.getReadyPatternGroup().getId(), rp.getOrderInGroup()));
			}
			Collections.sort(patternDtoList, comp);
			groupDtoList.add(new ReadyPatternGroupResDto(group.getId(), group.getName(), patternDtoList));
		}
		
		System.out.println("TAG : " + groupDtoList);
		return groupDtoList;
	}
	
	@Transactional
	public void deleteGroupById(int gid) {
		readyPatternGroupRepository.deleteById(gid);
	}
	
	public class pComparator implements Comparator<ReadyPatternResDto> {
		@Override
		public int compare(ReadyPatternResDto o1, ReadyPatternResDto o2) {
			int firstValue = o1.getOrderInGroup();
			int secondValue = o2.getOrderInGroup();
			
			/*Order By Ascending*/
			if (firstValue > secondValue) {
				return 1;
			} else if (firstValue < secondValue) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}
	
}
