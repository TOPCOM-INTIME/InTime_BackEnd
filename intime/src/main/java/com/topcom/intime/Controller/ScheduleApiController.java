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
import com.topcom.intime.Dto.UserResDto;
import com.topcom.intime.Dto.Schedule.SaveScheduleDto;
import com.topcom.intime.Dto.Schedule.ScheduleResDto;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.model.User;
import com.topcom.intime.service.ReadyPatternService;
import com.topcom.intime.service.SchedulePoolService;
import com.topcom.intime.service.ScheduleService;

import io.swagger.annotations.ApiOperation;

@RestController
public class ScheduleApiController {
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	SchedulePoolService schedulePoolService;
	
	@Autowired
	ReadyPatternService readyPatternService;
	
	@ApiOperation(value = "Save a Individual Schedule")
	@PostMapping("/api/schedule") 
	public ResponseDto<Integer> SaveIndividualSchedule(@RequestBody SaveScheduleDto schedule) {
		
		System.out.println("TAGG1 : " + schedule);
		
		Schedule saved_schedule = scheduleService.save_schedule(getPrincipal(), schedule, null);
		
		readyPatternService.save_pattern_in_schedule(schedule.getReadyPatterns_Ids(), saved_schedule);
			
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@ApiOperation(value = "Save a Group-Schedule", notes = "If you save a group-schedule, DB will be add Schedule, SchedulePool and SchedulePoolId automatically.")
	@PostMapping("/api/group-schedule")
	public ResponseDto<Integer> SaveGroupSchedule(@RequestBody SaveScheduleDto schedule) {
	
		User request_user = getPrincipal();
		int uid = request_user.getId();
		
		Schedule savedSchedule = scheduleService.save_schedule(request_user, schedule, null);
		
		String schedulePoolId = "user" + uid + "-" + savedSchedule.getId();

		schedulePoolService.save_pool(schedulePoolId, savedSchedule.getName(), savedSchedule.getTime(), savedSchedule.getDestName());
		scheduleService.update_schedulePoolId(savedSchedule.getId(), schedulePoolId);
		schedulePoolService.AddMemberInSchedule(schedulePoolId, uid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);

	}

	@ApiOperation(value = "Save group-schedule after accepting an invitation.", 
			notes = "If you accept the invitation, you make new schedule on your device. After that, it is totally acception of invitation. And DB will be updated as well.")
	@PostMapping("/api/group-scehduel-after-invitation/schedulepool={pid}")
	public ResponseDto<Integer> saveGroupScheduleAfterInvited(@RequestBody SaveScheduleDto scheduleDto, @PathVariable("pid")String pid) {
		
		User request_user = getPrincipal();
		
		scheduleService.save_schedule(request_user, scheduleDto, pid);
		schedulePoolService.AddMemberInSchedule(pid, request_user.getId());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@ApiOperation(value = "Update Schedule Details.", notes = "You should not put null on any parameter.")
	@PutMapping("/api/schedule={id}/update")
	public ResponseDto<Integer> UpdateSchedule(@PathVariable("id")int id, @RequestBody SaveScheduleDto scheduleDto) {
		
		scheduleService.update_ScheduleById(id, scheduleDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@ApiOperation(value = "Get all schedules made by user.", notes = "")
	@GetMapping("/api/user/schedule/all")
	public List<ScheduleResDto> findSchedulesByUid() {
		
		return scheduleService.findSchedulesByUid(getPrincipal().getId());
	}
	
	@ApiOperation(value = "Not used.")
	@GetMapping("/api/schedule2")
	public List<Schedule> findSchedulesByUid2() {
		
		return scheduleService.findSchedulesByUid2(getPrincipal().getId());
	}
	
	@ApiOperation(value = "Get all schedules belong to a Pool"
			, notes = "If you create group-schedule, schedulePool is created as well. SchedulePool cantains information of schedules made by each partners of schedule"
					+ "and information of partners. It is entity as bridege connecting schedules and partners(members).")
	@GetMapping("/api/schedulePool={pid}/schedules")
	public List<ScheduleResDto> findSchedulesInPoolById(@PathVariable("pid")String pid) {
		return schedulePoolService.findSchedulesInPoolById(pid);
	}
	
	@ApiOperation(value = "Get all members belong to a Pool"
			, notes = "If you create group-schedule, schedulePool is created as well. SchedulePool cantains information of schedules made by each partners of schedule"
					+ "and information of partners. It is entity as bridege connecting schedules and partners(members).")
	@GetMapping("/api/schedulePools={pid}/members")
	public List<UserResDto> findMembersInSchedule(@PathVariable("pid")String pid) {
		return schedulePoolService.findMembersInPoolByPid(pid);
	}
	
	@ApiOperation(value = "Delete a schedule")
	@DeleteMapping("/api/schedule/scheduleId={sid}")
	public ResponseDto<Integer> DeleteScheduleById(@PathVariable("sid") int sid) {
		
		scheduleService.delete_scheduleById(sid);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@ApiOperation(value = "Delete a schedulePool")
	@DeleteMapping("/api/schedulePool={id}")
	public ResponseDto<Integer> DeleteSchedulePoolById(@PathVariable("id") String id) {
		
		schedulePoolService.delete_schedulePoolById(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	public User getPrincipal() {
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		PrincipalDetails principal = (PrincipalDetails) principalObject;
		return principal.getUser();
	}
}
