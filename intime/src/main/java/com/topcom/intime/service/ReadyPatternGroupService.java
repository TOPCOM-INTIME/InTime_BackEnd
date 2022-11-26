package com.topcom.intime.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topcom.intime.Dto.ReadyPattern.PatternGroupResDto;
import com.topcom.intime.Dto.ReadyPattern.PatternResDto;
import com.topcom.intime.Dto.ReadyPattern.SaveOnePatternGroupDto;
import com.topcom.intime.Dto.ReadyPattern.SavePatternInGroupDto;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.ReadyPatternGroup;
import com.topcom.intime.repository.ReadyPatternGroupRepository;
import com.topcom.intime.repository.ReadyPatternRepository;

@Service
public class ReadyPatternGroupService {
	
	@Autowired
	private ReadyPatternRepository readyPatternRepository;
	
	@Autowired
	private ReadyPatternGroupRepository readyPatternGroupRepository;
	
	@Transactional
	public int save_group(int uid, SaveOnePatternGroupDto groupReqDto) {
//		System.out.println("TAG : " + groupReqDto);
		List<Integer> temp = new ArrayList<>();
		ReadyPatternGroup rpg = ReadyPatternGroup.builder()
				.name(groupReqDto.getName())
				.userId(uid)
				.build();
		int saved_id = readyPatternGroupRepository.save(rpg).getId();
		
		return saved_id;
	}
	
	@Transactional
	public void save_patternList(int uid, int gid, List<Integer> patternIdList) {
		ReadyPatternGroup patternGroup = readyPatternGroupRepository.findById(gid)
		.orElseThrow(()->{
			return new IllegalArgumentException("Failed to find PatternGRoup by id: " + gid);
		});
		patternGroup.setReadyPatterns_Ids(patternIdList);
	}
	
	@Transactional
	public void updateGroupById(int gid, SavePatternInGroupDto groupReqDto) {

		ReadyPatternGroup patternGroup = readyPatternGroupRepository.findById(gid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPattenGroup by id: " + gid);
				});
		patternGroup.setName(groupReqDto.getName());
		patternGroup.setReadyPatterns_Ids(groupReqDto.getPatterns_Ids());
	}
	
	@Transactional
	public List<PatternResDto> findPatternsByGid(int uid, int gid) {
		ReadyPatternGroup patternGroup = readyPatternGroupRepository.findById(gid)
				.orElseThrow(()->{
					return new IllegalArgumentException("Failed to find ReadyPattenGroup by id: " + gid);
				});
		List<PatternResDto> dtoList = new ArrayList<>();
		for (int pid: patternGroup.getReadyPatterns_Ids()) {
			ReadyPattern rp = readyPatternRepository.findById(pid)
					.orElseThrow(()->{
						return new IllegalArgumentException("Failed to find ReadyPatten by id: " + pid);
					});
			
			dtoList.add(new PatternResDto(rp.getId(), rp.getName(), rp.getTime(),
					rp.getUserId(), rp.getIsInGroup(), rp.getOrderInSchedule()));
		}
//		System.out.println("TAG : " + dtoLIst);
		return dtoList;
	}
	
	@Transactional
	public List<PatternGroupResDto> findAllGroupsByUid(int uid) {
		
		List<ReadyPatternGroup> groups = readyPatternGroupRepository.findAllByUid(uid);
		List<PatternGroupResDto> groupDtoList = new ArrayList<>();
		
//		pComparator comp = new pComparator();
		
		for (ReadyPatternGroup group : groups) {
			List<PatternResDto> patternDtoList = new ArrayList<>();
			List<Integer> patternIdList = group.getReadyPatterns_Ids();
			if (patternIdList == null) {
				groupDtoList.add(new PatternGroupResDto(group.getId(), group.getName(), null));
				continue;
			}
			for (int pid: patternIdList) {
				ReadyPattern rp = readyPatternRepository.findById(pid)
						.orElseThrow(()->{
							return new IllegalArgumentException("Failed to find ReadyPatten by id: " + pid);
						});
				System.out.println("TAG@@ : " + rp.getId());

				patternDtoList.add(new PatternResDto(rp.getId(), rp.getName(), rp.getTime(),
						rp.getUserId(), rp.getIsInGroup(), rp.getOrderInSchedule()));
			}
//			Collections.sort(patternDtoList, comp);
			groupDtoList.add(new PatternGroupResDto(group.getId(), group.getName(), patternDtoList));
		}
		
		System.out.println("TAG : " + groupDtoList);
		return groupDtoList;
	}
	
	@Transactional
	public boolean deletePatternById(int uid, int pid) {
		boolean is_contain = false;
		List<ReadyPatternGroup> patternGroupList = readyPatternGroupRepository.findAllByUid(uid);
		for (ReadyPatternGroup patternGroup : patternGroupList) {
			List<Integer> patternIds = patternGroup.getReadyPatterns_Ids();
			if (patternIds == null) {
				continue;
			}
			is_contain = patternIds.contains(pid);
		}
		if (is_contain == false) {
			readyPatternRepository.deleteById(pid);
		}
		return is_contain;
	}
	
	@Transactional
	public void deleteGroupById(int gid) {
		readyPatternGroupRepository.deleteById(gid);
	}
	
	public class pComparator implements Comparator<PatternResDto> {
		@Override
		public int compare(PatternResDto o1, PatternResDto o2) {
			int firstValue = o1.getOrderInSchedule();
			int secondValue = o2.getOrderInSchedule();
			
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
