package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.Schedule;
import com.school.sba.requestdto.ScheduleRequest;
import com.school.sba.responsedto.ScheduleResponse;
import com.school.sba.service.ScheduleService;
import com.school.sba.util.ResponseStructure;

@RestController
public class ScheduleController {

	@Autowired
	private ScheduleService scheduleService;

	@PostMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule( @PathVariable int schoolId,@RequestBody ScheduleRequest scheduleRequest){
		
		return scheduleService.addSchedule(schoolId,scheduleRequest);
	}

	@GetMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> getSchedule(@PathVariable int schoolId){
		
		return scheduleService.getSchedule(schoolId);
	}
	@PutMapping("/schedules/{scheduleId}")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(@PathVariable int scheduleId , @RequestBody ScheduleRequest scheduleRequest){
		return scheduleService.updateSchedule(scheduleId,scheduleRequest);
	}
	@DeleteMapping("/")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> deleteSchedule(Schedule schedule){
		return scheduleService.deleteSchedule(schedule);
	}
}
