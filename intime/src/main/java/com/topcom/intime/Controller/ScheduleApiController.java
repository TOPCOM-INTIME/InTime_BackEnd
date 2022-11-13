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

import com.topcom.intime.Dto.ResponseDto;
import com.topcom.intime.Dto.ScheduleMemberReqDto;
import com.topcom.intime.Dto.ScheduleReqDto;
import com.topcom.intime.Dto.ScheduleResDto;
import com.topcom.intime.Dto.UserResDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.service.SchedulePoolService;
import com.topcom.intime.service.ScheduleService;

@RestController
public class ScheduleApiController {
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	SchedulePoolService schedulePoolService;
	
	@PostMapping("/api/schedule") 
	public ResponseDto<Integer> SaveIndividualSchedule(@RequestBody ScheduleReqDto schedule, @AuthenticationPrincipal PrincipalDetails principal) {
		
		scheduleService.save_schedule(principal.getUser().getId(), schedule);
			
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/group-schedule")
	public ResponseDto<Integer> SaveGroupSchedule(@RequestBody ScheduleReqDto schedule, @AuthenticationPrincipal PrincipalDetails principal) {
		int uid = principal.getUser().getId();
		
		scheduleService.save_schedule(uid, schedule);
		Schedule savedSchedule = scheduleService.findLatestCreatedSchedule(uid);
		
		String schedulePoolId = "user" + uid + "-" + savedSchedule.getId();
		
		schedulePoolService.save_pool(schedulePoolId, savedSchedule.getTime(), savedSchedule.getDestName());
		scheduleService.update_schedulePoolId(savedSchedule.getId(), schedulePoolId);
		schedulePoolService.AddMemberInSchedule(schedulePoolId, uid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);

	}
	
	@PostMapping("/api/accept-schedule-invitation")
	public ResponseDto<Integer> AcceptScheduleInvitation(@RequestBody ScheduleMemberReqDto memberDto, @AuthenticationPrincipal PrincipalDetails principal) {
		
		schedulePoolService.AddMemberInSchedule(memberDto.getSchedulePoolId(), principal.getUser().getId());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/schedule={id}/update")
	public ResponseDto<Integer> UpdateSchedule(@PathVariable("id")int id, @RequestBody ScheduleReqDto scheduleDto) {
		
		scheduleService.update_ScheduleById(id, scheduleDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@GetMapping("/api/user/schedule/all")
	public List<ScheduleResDto> findSchedulesByUid(@AuthenticationPrincipal PrincipalDetails principal) {
		
		return scheduleService.findSchedulesByUid(principal.getUser().getId());
	}
	
	@GetMapping("/api/schedule2")
	public List<Schedule> findSchedulesByUid2(@AuthenticationPrincipal PrincipalDetails principal) {
		
		return scheduleService.findSchedulesByUid2(principal.getUser().getId());
	}
	
	@GetMapping("/api/schedulePool={pid}/schedules")
	public List<ScheduleResDto> findSchedulesInPoolById(@PathVariable("pid")String pid) {
		return schedulePoolService.findSchedulesInPoolById(pid);
	}
	
	@GetMapping("/api/schedulePools={pid}/members")
	public List<UserResDto> findMembersInSchedule(@PathVariable("pid")String pid) {
		return schedulePoolService.findMembersInPoolByPid(pid);
	}
	
	@DeleteMapping("/api/schedule/scheduleId={sid}")
	public ResponseDto<Integer> DeleteScheduleById(@PathVariable("sid") int sid) {
		
		scheduleService.delete_scheduleById(sid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/schedulePool={id}")
	public ResponseDto<Integer> DeleteSchedulePoolById(@PathVariable("id") String id) {
		
		schedulePoolService.delete_schedulePoolById(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
