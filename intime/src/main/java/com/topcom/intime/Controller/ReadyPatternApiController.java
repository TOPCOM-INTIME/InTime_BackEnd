package com.topcom.intime.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.Dto.ReadyPattern.PatternGroupResDto;
import com.topcom.intime.Dto.ReadyPattern.PatternResDto;
import com.topcom.intime.Dto.ReadyPattern.SaveOnePatternDto;
import com.topcom.intime.Dto.ReadyPattern.SaveOnePatternGroupDto;
import com.topcom.intime.Dto.ReadyPattern.SavePatternInGroupDto;
import com.topcom.intime.Dto.ReadyPattern.UpdateOrderDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.service.ReadyPatternGroupService;
import com.topcom.intime.service.ReadyPatternService;

import io.swagger.annotations.ApiOperation;

@RestController
public class ReadyPatternApiController {

	@Autowired
	ReadyPatternService readyPatternService;

	@Autowired
	ReadyPatternGroupService readyPatternGroupService;

	/* Pattern */
	@ApiOperation(value = "Save a Ready Pattern")
	@PostMapping("/api/readypattern")
	public ResponseDto<Integer> SavePattern(@RequestBody SaveOnePatternDto pattern) {

		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;

		readyPatternService.save_pattern(getPrincipalId(), pattern);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	//New
	@PostMapping("/api/PatternsWithGroup/groupId={gid}") // groupId path변수 빼도 될듯
	public ResponseDto<Integer> SavePatternsInGroup(@PathVariable("gid") int gid,
			@RequestBody SavePatternInGroupDto savePatternInGroupDto) {

		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;

		readyPatternGroupService.save_patternList(principal.getUser().getId(), gid, savePatternInGroupDto.getPatterns_Ids());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@ApiOperation(value = "Not used", notes = "")
	@PutMapping("/api/readypatterns/update-groupId/groupId={gid}")
	public ResponseDto<Integer> updateOrderOfPatterns(@PathVariable("gid") int gid,
			@RequestBody List<UpdateOrderDto> patternList) {

		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;

		readyPatternService.UpdateGroupIdOfPatterns(principal.getUser().getId(), gid, patternList);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@ApiOperation(value = "Update name or time of ready pattern.", notes = "You should not put null data on both name and time. ")
	@PutMapping("/api/readypattern/update-name-or-time/patternId={pid}")
	public ResponseDto<Integer> pUpdateNameOrTimeById(@PathVariable("pid") int id,
			@RequestBody SaveOnePatternDto readyPatternReqDto) {
		readyPatternService.updateNameOrTime(id, readyPatternReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@ApiOperation(value = "Get all pure ready patterns.", notes = "Get all ready patterns only not in group.")
	@GetMapping("/api/readypatterns/origin")
	public List<PatternResDto> findOriginPatternsByUid() {

		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;

		return readyPatternService.findOriginsByUid(principal.getUser().getId());
	}

	@ApiOperation(value = "Get ready patterns in Group", notes = "")
	@GetMapping("/api/groupId={gid}/readypatterns")
	public List<PatternResDto> findAllPatternsInGroupById(@PathVariable("gid") int gid) {

		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;

		return readyPatternGroupService.findPatternsByGid(principal.getUser().getId(), gid);
	}

	@ApiOperation(value = "Delete ready pattern", notes = "")
	@DeleteMapping("/api/readypattern/patternId={pid}")
	public ResponseDto<Integer> deletePatternById(@PathVariable("pid") int pid) {
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;

		boolean is_contain = 
				readyPatternGroupService.deletePatternById(principal.getUser().getId(), pid);
		if (is_contain) {
			return new ResponseDto<Integer>(HttpStatus.FAILED_DEPENDENCY.value(), -1);
		} else {
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
		}
	}

	/* Group */
	@ApiOperation(value = "Save a ready pattern group.", notes = "")
	@PostMapping("/api/patterngroup")
	public ResponseDto<Integer> SaveGroup(@RequestBody SaveOnePatternGroupDto groupReqDto) {

		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;

		int response_id = readyPatternGroupService.save_group(principal.getUser().getId(), groupReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), response_id);
	}

	@ApiOperation(value = "Update group.", notes = "")
	@PutMapping("/api/update-group/groupId={gid}")
	public ResponseDto<Integer> UpdatePatternsInGroup(@PathVariable("gid") int gid,
			@RequestBody SavePatternInGroupDto savePatternInGroupDto) {

		readyPatternGroupService.updateGroupById(gid, savePatternInGroupDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@ApiOperation(value = "Get all groups and patterns.", notes = "Get all groups and patterns in groups.")
	@GetMapping("/api/groups-with-patterns/all")
	public List<PatternGroupResDto> findAllGroupsByUid() {

		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;

		return readyPatternGroupService.findAllGroupsByUid(principal.getUser().getId());
	}

	@ApiOperation(value = "Delete pattern group.", notes = "If you delete pattern group, patterns in that group will be deleted as well.")
	@DeleteMapping("/api/patterngroup/groupId={gid}")
	public ResponseDto<Integer> DeleteGroupById(@PathVariable("gid") int gid) {

		readyPatternGroupService.deleteGroupById(gid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	public int getPrincipalId() {
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;
		return principal.getUser().getId();
	}

}
