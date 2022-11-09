package com.topcom.intime.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.Dto.ReadyPatternGroupReqDto;
import com.topcom.intime.Dto.ReadyPatternGroupResDto;
import com.topcom.intime.Dto.ReadyPatternReqDto;
import com.topcom.intime.Dto.ReadyPatternResDto;
import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.ReadyPatternGroup;
import com.topcom.intime.service.ReadyPatternGroupService;
import com.topcom.intime.service.ReadyPatternService;

@RestController
public class ReadyPatternApiController {

	@Autowired
	ReadyPatternService readyPatternService;
	
	@Autowired
	ReadyPatternGroupService readyPatternGroupService;
	
	/*Pattern*/
	@PostMapping("/api/userId={id}/readypattern")
	public ResponseDto<Integer> SavePattern(@PathVariable("id")int uid, @RequestBody ReadyPatternReqDto pattern) {

		readyPatternService.save_pattern(uid, pattern);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/userId={uid}/PatternsWithGroup/groupId={gid}")//groupId path변수 빼도 될듯
	public ResponseDto<Integer> SavePatternsWithGroup(@PathVariable("uid")int uid, @PathVariable("gid")int gid, @RequestBody List<ReadyPatternReqDto> patternList) {
		
		System.out.println("TAG : " + patternList);
		readyPatternService.save_patternList(uid, gid, patternList);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/userId={uid}/readypatterns/update-groupId/groupId={gid}")
	public ResponseDto<Integer> UpdateGroupIdOfPatterns(@PathVariable("uid")int uid, @PathVariable("gid") int gid, @RequestBody List<ReadyPatternReqDto> patternList) {

		readyPatternService.UpdateGroupIdOfPatterns(uid, gid, patternList);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/userId={uid}/readypattern/update-name-or-time/patternId={pid}")
	public ResponseDto<Integer> pUpdateNameOrTimeById(@PathVariable("uid")int uid, @PathVariable("pid") int id, @RequestBody ReadyPatternReqDto readyPatternReqDto) {
		readyPatternService.updateNameOrTime(id, readyPatternReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

//	@PutMapping("/api/readypattern/{readypatternId}/ingroup/{groupId}")
//	public ResponseDto<Integer> pGetInGroup(@PathVariable("readypatternId")int pid, @PathVariable("groupId")int gid, @RequestBody ReadyPatternReqDto readyPatternReqDto) {
//		readyPatternService.getInGroup(pid, gid, readyPatternReqDto);
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	@GetMapping("/api/userId={uid}/readypatterns/all")
	public List<ReadyPatternResDto> findAllPatternsByUid(@PathVariable("uid") int uid) {
		
		return readyPatternService.findAllByUid(uid);
	}
	
	@GetMapping("/api/userId={uid}/readypatterns/origin")
	public List<ReadyPatternResDto> findOriginPatternsByUid(@PathVariable("uid") int uid) {
		
		return readyPatternService.findOriginsByUid(uid);
	}
	
	@GetMapping("/api/userId={uid}/groupId={gid}/readypatterns")
	public List<ReadyPatternResDto> findAllPatternsInGroupById(@PathVariable("uid")int uid, @PathVariable("gid")int gid){

		return readyPatternGroupService.findPatternsByGid(uid, gid);
	}
	
	@DeleteMapping("/api/userId={uid}/readypattern/patternId={pid}")
	public ResponseDto<Integer> deletePatternById(@PathVariable("uid")int uid, @PathVariable("pid")int pid) {
		readyPatternService.deletePatternById(pid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*Group*/
	@PostMapping("/api/userId={uid}/patterngroup")
	public ResponseDto<Integer> SaveGroup(@PathVariable("uid")int uid, @RequestBody ReadyPatternGroupReqDto groupReqDto) {
		
		readyPatternGroupService.save_group(uid, groupReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/userId={uid}/update-group-name/groupId={gid}")
	public ResponseDto<Integer> UpdateGroupName(@PathVariable("uid")int uid, @PathVariable("gid")int gid, @RequestBody ReadyPatternGroupReqDto groupReqDto) {
		
		readyPatternGroupService.updateGroupNameById(gid, groupReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@GetMapping("/api/userId={uid}/groups-with-patterns/all")
	public List<ReadyPatternGroupResDto> findAllGroupsByUid (@PathVariable("uid") int uid) {
		
		return readyPatternGroupService.findAllGroupsByUid(uid);
	}
	
	@DeleteMapping("/api/userId={uid}/patterngroup/groupId={gid}")
	public ResponseDto<Integer> DeleteGroupById(@PathVariable("uid")int uid, @PathVariable("gid")int gid) {
		
		readyPatternGroupService.deleteGroupById(gid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
}
