package com.topcom.intime.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.service.ReadyPatternGroupService;
import com.topcom.intime.service.ReadyPatternService;

@RestController
public class ReadyPatternApiController {

	@Autowired
	ReadyPatternService readyPatternService;
	
	@Autowired
	ReadyPatternGroupService readyPatternGroupService;
	
	/*Pattern*/
	@PostMapping("/api/readypattern")
	public ResponseDto<Integer> SavePattern(@RequestBody ReadyPatternReqDto pattern, @AuthenticationPrincipal PrincipalDetails principal) {

		readyPatternService.save_pattern(principal.getUser().getId(), pattern);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/PatternsWithGroup/groupId={gid}")//groupId path변수 빼도 될듯
	public ResponseDto<Integer> SavePatternsWithGroup(@AuthenticationPrincipal PrincipalDetails principal,@PathVariable("gid")int gid, @RequestBody List<ReadyPatternReqDto> patternList) {
		
		System.out.println("TAG : " + patternList);
		readyPatternService.save_patternList(principal.getUser().getId(), gid, patternList);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/readypatterns/update-groupId/groupId={gid}")
	public ResponseDto<Integer> UpdateGroupIdOfPatterns(@AuthenticationPrincipal PrincipalDetails principal, @PathVariable("gid") int gid, @RequestBody List<ReadyPatternReqDto> patternList) {

		readyPatternService.UpdateGroupIdOfPatterns(principal.getUser().getId(), gid, patternList);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/readypattern/update-name-or-time/patternId={pid}")
	public ResponseDto<Integer> pUpdateNameOrTimeById(@PathVariable("pid") int id, @RequestBody ReadyPatternReqDto readyPatternReqDto) {
		readyPatternService.updateNameOrTime(id, readyPatternReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

//	@PutMapping("/api/readypattern/{readypatternId}/ingroup/{groupId}")
//	public ResponseDto<Integer> pGetInGroup(@PathVariable("readypatternId")int pid, @PathVariable("groupId")int gid, @RequestBody ReadyPatternReqDto readyPatternReqDto) {
//		readyPatternService.getInGroup(pid, gid, readyPatternReqDto);
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	@GetMapping("/api/readypatterns/all")
	public List<ReadyPatternResDto> findAllPatternsByUid(@AuthenticationPrincipal PrincipalDetails principal) {
		
		return readyPatternService.findAllByUid(principal.getUser().getId());
	}
	
	@GetMapping("/api/readypatterns/origin")
	public List<ReadyPatternResDto> findOriginPatternsByUid(@AuthenticationPrincipal PrincipalDetails principal) {
		
		return readyPatternService.findOriginsByUid(principal.getUser().getId());
	}
	
	@GetMapping("/api/groupId={gid}/readypatterns")
	public List<ReadyPatternResDto> findAllPatternsInGroupById(@AuthenticationPrincipal PrincipalDetails principal, @PathVariable("gid")int gid){

		return readyPatternGroupService.findPatternsByGid(principal.getUser().getId(), gid);
	}
	
	@DeleteMapping("/api/readypattern/patternId={pid}")
	public ResponseDto<Integer> deletePatternById(@PathVariable("pid")int pid) {
		readyPatternService.deletePatternById(pid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*Group*/
	@PostMapping("/api/patterngroup")
	public ResponseDto<Integer> SaveGroup(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody ReadyPatternGroupReqDto groupReqDto) {
		
		readyPatternGroupService.save_group(principal.getUser().getId(), groupReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/update-group-name/groupId={gid}")
	public ResponseDto<Integer> UpdateGroupName(@PathVariable("gid")int gid, @RequestBody ReadyPatternGroupReqDto groupReqDto) {
		
		readyPatternGroupService.updateGroupNameById(gid, groupReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@GetMapping("/api/groups-with-patterns/all")
	public List<ReadyPatternGroupResDto> findAllGroupsByUid (@AuthenticationPrincipal PrincipalDetails principal) {
		
		return readyPatternGroupService.findAllGroupsByUid(principal.getUser().getId());
	}
	
	@DeleteMapping("/api/patterngroup/groupId={gid}")
	public ResponseDto<Integer> DeleteGroupById(@PathVariable("gid")int gid) {
		
		readyPatternGroupService.deleteGroupById(gid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
}
