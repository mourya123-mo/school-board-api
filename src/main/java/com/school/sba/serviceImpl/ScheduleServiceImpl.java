package com.school.sba.serviceImpl;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.exception.ConstraintVoilationException;
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
		return ScheduleResponse.builder().opeansAt(schedule.getOpeansAt()).closesAt(schedule.getClosesAt())
				.classHoursPerDay(schedule.getClassHoursPerDay()).scheduleId(schedule.getScheduleId())
				.breakLengthInMinutes((int) schedule.getBreakLengthInMinutes().toMinutes())
				.breakTime(schedule.getBreakTime())
				.breakLengthInMinutes((int) schedule.getBreakLengthInMinutes().toMinutes())
				.lunchTime(schedule.getLunchTime())
				.lunchLengthInMinutes((int) schedule.getLunchLengthInMinutes().toMinutes()).build();
	}

	private Schedule mapToSchedule(ScheduleRequest scheduleRquest) {
		return Schedule.builder().opeansAt(scheduleRquest.getOpeansAt()).closesAt(scheduleRquest.getClosesAt())
				.classHoursPerDay(scheduleRquest.getClassHoursPerDay())
				.breakLengthInMinutes(Duration.ofMinutes(scheduleRquest.getBreakLengthInMinutes()))
				.breakTime(scheduleRquest.getBreakTime())
				.breakLengthInMinutes(Duration.ofMinutes(scheduleRquest.getBreakLengthInMinutes()))
				.lunchTime(scheduleRquest.getLunchTime())
				.lunchLengthInMinutes(Duration.ofMinutes(scheduleRquest.getLunchLengthInMinutes())).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule(int schoolId,
			ScheduleRequest scheduleRequest) {
		School school = schoolRepo.findById(schoolId)
				.orElseThrow(() -> new UserNotFoundByIdException("School not present in database"));
		if (school.getSchedule() == null) {
			Schedule schedule = scheduleRepo.save(mapToSchedule(scheduleRequest));
			school.setSchedule(schedule);
			schoolRepo.save(school);
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("schedule update sucessful");
			structure.setData(mapToScheduleResponse(schedule));
		} else {
			throw new ConstraintVoilationException("sdfjkghfsd");
		}
		return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> getSchedule(int schoolId) {

		return schoolRepo.findById(schoolId).map(school -> {
			if (school.getSchedule() != null) {
				structure.setStatus(HttpStatus.FOUND.value());
				structure.setMessage("schedule data found");
				structure.setData(mapToScheduleResponse(school.getSchedule()));
				return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure, HttpStatus.FOUND);
			} else {
				throw new ScheduleNotFoundByIdException("schedule data not found");
			}
		}).orElseThrow(() -> new SchoolNotFoundByIdException("school data not found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId,
			ScheduleRequest scheduleRequest) {

		return scheduleRepo.findById(scheduleId).map(schedule -> {
			Schedule schedule2 = mapToSchedule(scheduleRequest);
			schedule2.setScheduleId(schedule.getScheduleId());
			schedule2 = scheduleRepo.save(schedule2);
			ScheduleResponse scheduleResponse = mapToScheduleResponse(schedule2);
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("schedule data updated sucessfully");
			structure.setData(scheduleResponse);
			return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure, HttpStatus.OK);
		}).orElseThrow(() -> new ScheduleNotFoundByIdException("schedule data not found by id"));
	}

}
