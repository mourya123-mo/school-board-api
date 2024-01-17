package com.school.sba.serviceImpl;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.exception.ScheduleNotFoundByIdException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.ScheduleRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.requestdto.ScheduleRequest;
import com.school.sba.responsedto.ScheduleResponse;
import com.school.sba.service.ScheduleService;
import com.school.sba.util.ResponseStructure;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleRepo scheduleRepo;
	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	ResponseStructure<ScheduleResponse> structure;

	private ScheduleResponse mapToScheduleResponse(Schedule schedule) {
		return ScheduleResponse.builder().opensAt(schedule.getOpensAt()).closesAt(schedule.getClosesAt())
				.classHourPerDay(schedule.getClassHourPerDay())
				.scheduleId(schedule.getScheduleId())
				.classHourLengthInMinutes(schedule.getClassHourLengthInMinutes())
				.breakTime(schedule.getBreakTime())
				.breakLengthInMinutes(schedule.getBreakLengthInMinutes())
				.lunchTime(schedule.getLunchTime())
				.lunchLengthInMinutes(schedule.getLunchLengthInMinutes()).build();
	}

	private Schedule mapToSchedule(ScheduleRequest scheduleRquest) {
		return Schedule.builder().opensAt(scheduleRquest.getOpensAt()).closesAt(scheduleRquest.getClosesAt())
				.classHourPerDay(scheduleRquest.getClassHourPerDay())
				.breakLengthInMinutes(Duration.ofMinutes(scheduleRquest.getBreakLengthInMinutes()))
				.breakTime(scheduleRquest.getBreakTime())
				.breakLengthInMinutes(Duration.ofMinutes(scheduleRquest.getBreakLengthInMinutes()))
				.lunchTime(scheduleRquest.getLunchTime())
				.lunchLengthInMinutes(Duration.ofMinutes(scheduleRquest.getLunchLengthInMinutes())).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule(int schoolId,
			ScheduleRequest scheduleRequest) {
		School school=	schoolRepo.findById(schoolId).orElseThrow(()-> new UserNotFoundByIdException("School not present in database"));
			Schedule schedule	=scheduleRepo.save(mapToSchedule(scheduleRequest));
			school.setSchedule(schedule);
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("schedule update sucessful");
			structure.setData(mapToScheduleResponse(schedule));
		return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> getSchedule(int schoolId) {
		
		return schoolRepo.findById(schoolId).map(school->{
			if(school.getSchedule()!=null) {
				structure.setStatus(HttpStatus.FOUND.value());
				structure.setMessage("schedule data found");
				structure.setData(mapToScheduleResponse(school.getSchedule()));
				return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.FOUND);
			}else {
				throw new ScheduleNotFoundByIdException("schedule data not found");
			}
		}).orElseThrow(()-> new SchoolNotFoundByIdException("school data not found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId,
			ScheduleRequest scheduleRequest) {
		
		return scheduleRepo.findById(scheduleId).map(schedule->{
			Schedule schedule2 = mapToSchedule(scheduleRequest);
			schedule2.setScheduleId(schedule.getScheduleId());
		  schedule2 = scheduleRepo.save(schedule2);
			  ScheduleResponse scheduleResponse = mapToScheduleResponse(schedule2);
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("schedule data updated sucessfully");
			structure.setData(scheduleResponse);
			return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.OK);
		}).orElseThrow(()-> new ScheduleNotFoundByIdException("schedule data not found by id"));
	}

}
