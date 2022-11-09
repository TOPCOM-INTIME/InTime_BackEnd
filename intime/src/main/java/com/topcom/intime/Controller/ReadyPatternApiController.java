package com.topcom.intime.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	@PostMapping("/api/readypattern")
	public ResponseDto<Integer> pSave(@RequestBody ReadyPatternReqDto readyPatternReqDto) {
		System.out.println("TAG : " + readyPatternReqDto);

		readyPatternService.create(readyPatternReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/readypatterns")//groupId path변수 빼도 될듯
	public ResponseDto<Integer> pListSave(@RequestBody List<ReadyPatternReqDto> patternReqDtoList) {
		
		System.out.println("TAG : " + patternReqDtoList);
		readyPatternService.create2(patternReqDtoList);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/readypatterns/update-groupid/{groupId}")
	public ResponseDto<Integer> pUpdate(@PathVariable("groupId") int gid, @RequestBody List<ReadyPatternReqDto> patternReqDtoList) {
		System.out.println("TAG : " + gid + " : " + patternReqDtoList);

		readyPatternService.updateGroupId(gid, patternReqDtoList);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/readypattern/update-name-or-time/{readypatternId}")
	public ResponseDto<Integer> pUpdateNameOrTimeById(@PathVariable("readypatternId") int id, @RequestBody ReadyPatternReqDto readyPatternReqDto) {
		readyPatternService.updateNameOrTime(id, readyPatternReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

//	@PutMapping("/api/readypattern/{readypatternId}/ingroup/{groupId}")
//	public ResponseDto<Integer> pGetInGroup(@PathVariable("readypatternId")int pid, @PathVariable("groupId")int gid, @RequestBody ReadyPatternReqDto readyPatternReqDto) {
//		readyPatternService.getInGroup(pid, gid, readyPatternReqDto);
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	@GetMapping("/api/user/{userId}/readypatterns")
	public List<ReadyPatternResDto> findPatternsByUid(@PathVariable("userId") int uid) {
		
		return readyPatternService.findAllByUid(uid);
	}
	
	@GetMapping("/api/patterngroup/{groupId}/readypatterns")
	public List<ReadyPatternResDto> findAllPatternsInGroupById(@PathVariable("groupId")int id){

		return readyPatternGroupService.findPatternsByGid(id);
	}
	
	/*Group*/
	@PostMapping("/api/patterngroup")
	public ResponseDto<Integer> gSave(@RequestBody ReadyPatternGroupReqDto groupReqDto) {
		
		readyPatternGroupService.create(groupReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("api/patterngroup/{groupId}")
	public ResponseDto<Integer> gUpdateName(@PathVariable("groupId")int id, @RequestBody ReadyPatternGroupReqDto groupReqDto) {
		
		readyPatternGroupService.updateName(id, groupReqDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@GetMapping("/api/user/{userId}/patterngroups")
	public List<ReadyPatternGroupResDto> findAllGroupsByUid (@PathVariable("userId") int uid) {
		
		return readyPatternGroupService.findAllGroupsByUid(uid);
	}
	
	
}
