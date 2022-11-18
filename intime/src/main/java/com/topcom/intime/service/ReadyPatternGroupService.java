package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.ReadyPattern.PatternGroupResDto;
import com.topcom.intime.Dto.ReadyPattern.PatternResDto;
import com.topcom.intime.Dto.ReadyPattern.SaveOnePatternGroupDto;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.ReadyPatternGroup;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.ReadyPatternGroupRepository;

@Service
public class ReadyPatternGroupService {
	
	@Autowired
	private ReadyPatternGroupRepository readyPatternGroupRepository;
	
	@Transactional
	public int save_group(User user, SaveOnePatternGroupDto groupReqDto) {
//		readyPatternGroupRepository.mSave(groupReqDto.getName(), uid);
		ReadyPatternGroup rpg = ReadyPatternGroup.builder().name(groupReqDto.getName()).user(user).build();
		int saved_id = readyPatternGroupRepository.save(rpg).getId();
		
		return saved_id;
	}
	
	@Transactional
	public void updateGroupNameById(int id, SaveOnePatternGroupDto groupReqDto) {

		ReadyPatternGroup patternGroup = readyPatternGroupRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPatten by id: " + id);
				});
		patternGroup.setName(groupReqDto.getName());
	}
	
	@Transactional
	public List<PatternResDto> findPatternsByGid(int uid, int gid) {
		ReadyPatternGroup patternGroup = readyPatternGroupRepository.findById(gid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPattenGroup by id: " + gid);
				});
		List<PatternResDto> dtoLIst = new ArrayList<>();
		for (ReadyPattern rp: patternGroup.getReadyPatterns()) {
			dtoLIst.add(new PatternResDto(rp.getId(), rp.getName(), rp.getTime(), rp.getUser().getId(), rp.getReadyPatternGroup().getId(), rp.getOrderInGroup()));
		}
//		System.out.println("TAG : " + dtoLIst);
		return dtoLIst;
	}
	
	@Transactional
	public List<PatternGroupResDto>  findAllGroupsByUid(int uid) {
		
		List<ReadyPatternGroup> groups = readyPatternGroupRepository.findAllByUid(uid);
		List<PatternGroupResDto> groupDtoList = new ArrayList<>();
		
		pComparator comp = new pComparator();
		
		for (ReadyPatternGroup group : groups) {
			List<PatternResDto> patternDtoList = new ArrayList<>();
			for(ReadyPattern rp : group.getReadyPatterns()) {
				patternDtoList.add(new PatternResDto(rp.getId(), rp.getName(), rp.getTime(), rp.getUser().getId(), rp.getReadyPatternGroup().getId(), rp.getOrderInGroup()));
			}
			Collections.sort(patternDtoList, comp);
			groupDtoList.add(new PatternGroupResDto(group.getId(), group.getName(), patternDtoList));
		}
		
		System.out.println("TAG : " + groupDtoList);
		return groupDtoList;
	}
	
	@Transactional
	public void deleteGroupById(int gid) {
		readyPatternGroupRepository.deleteById(gid);
	}
	
	public class pComparator implements Comparator<PatternResDto> {
		@Override
		public int compare(PatternResDto o1, PatternResDto o2) {
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
