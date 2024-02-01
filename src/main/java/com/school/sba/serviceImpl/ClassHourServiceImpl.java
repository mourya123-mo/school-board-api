package com.school.sba.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.ClassStatus;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AlreadyClassHourAssoatedException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SubjectRepoitory;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.service.ClassHourService;
import com.school.sba.util.ResponseStructure;

@Service
public class ClassHourServiceImpl implements ClassHourService {
	@Autowired
	private ClassHourRepo classHourRepo;
	@Autowired
	private AcademicProgramRepo academicProgramRepo;
	@Autowired
	private ClassHourRequest classHourRequest;
	@Autowired
	private SubjectRepoitory subjectRepo;
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ResponseStructure<ClassHourRequest> structure;

	private boolean isBreakTime(LocalDateTime currentTime, Schedule schedule) {
		LocalTime breakTimeStart = schedule.getBreakTime();
		LocalTime breakTimeEnd = breakTimeStart.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());

		return (currentTime.toLocalTime().isAfter(breakTimeStart) && currentTime.toLocalTime().isBefore(breakTimeEnd));

	}

	private boolean isLunchTime(LocalDateTime currentTime, Schedule schedule) {
		LocalTime lunchTimeStart = schedule.getLunchTime();
		LocalTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());

		return (currentTime.toLocalTime().isAfter(lunchTimeStart) && currentTime.toLocalTime().isBefore(lunchTimeEnd));

	}

	@Override
	public ResponseEntity<ResponseStructure<ClassHour>> createClassHour(int programId) {
		return academicProgramRepo.findById(programId).map(academicprogram -> {
			School school = academicprogram.getSchool();
			Schedule schedule = school.getSchedule();
			if (schedule != null) {
				int classHourPerDay = schedule.getClassHoursPerDay();
				int classHourLength = (int) schedule.getClassHoursLengthInMinutes().toMinutes();

				LocalDateTime currentTime = LocalDateTime.now().with(schedule.getOpeansAt());

				LocalTime lunchTimeStart = schedule.getLunchTime();
				LocalTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());
				LocalTime breakTimeStart = schedule.getBreakTime();
				LocalTime breakTimeEnd = breakTimeStart.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());
				int days;
				if (LocalDateTime.now().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
					days = 6;
				} else {
					days = 12;
				}
				for (int day = currentTime.getDayOfWeek().getValue(); day <= days; day++) {
					for (int hour = 0; hour < classHourPerDay + 2; hour++) {
						ClassHour classHour = new ClassHour();
						if (!currentTime.toLocalTime().equals(lunchTimeStart) && !isLunchTime(currentTime, schedule)) {
							if (!currentTime.toLocalTime().equals(breakTimeStart)
									&& !isBreakTime(currentTime, schedule)) {
								LocalDateTime beginsAt = currentTime;
								if (currentTime.toLocalDate().now().equals(DayOfWeek.SUNDAY)) {
									beginsAt = beginsAt.plusDays(1);
								}
								LocalDateTime endsAt = beginsAt.plusMinutes(classHourLength);

								classHour.setBeginsAt(beginsAt);
								classHour.setEndsAt(endsAt);
								classHour.setClassStatus(ClassStatus.NOT_SCHEDULED);

								currentTime = endsAt;
							} else {
								classHour.setBeginsAt(currentTime);
								classHour.setEndsAt(LocalDateTime.now().with(breakTimeEnd));
								classHour.setClassStatus(ClassStatus.BREAK_TIME);
								currentTime = currentTime.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());
							}
						} else {
							classHour.setBeginsAt(currentTime);
							classHour.setEndsAt(LocalDateTime.now().with(lunchTimeEnd));
							classHour.setClassStatus(ClassStatus.LUNCH_TIME);
							currentTime = currentTime.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());
						}
						classHour.setAcademicProgram(academicprogram);
						classHourRepo.save(classHour);
					}
					currentTime = currentTime.plusDays(1).with(schedule.getOpeansAt());
				}
			} else
				throw new UserNotFoundByIdException(
						"The school does not contain any schedule, provide a schedule to the school");

			return new ResponseEntity<ResponseStructure<ClassHour>>(HttpStatus.CREATED);

		}).orElseThrow(() -> new UserNotFoundByIdException("Invalid Program Id"));

	}

	@Override
	public ResponseEntity<ResponseStructure<ClassHourRequest>> updateClassHour(List<ClassHourRequest> updateRequest) {
		updateRequest.forEach((req) -> {
			User user = userRepo.findById(req.getUserId())
					.orElseThrow(() -> new UserNotFoundByIdException("given id not presnt in data base"));
			Subject subject = subjectRepo.findById(req.getSubjectId())
					.orElseThrow(() -> new UserNotFoundByIdException("given id not presnt in data base"));
			int roomNo = req.getRoomNo();
			ClassHour classHour = classHourRepo.findById(req.getClassHourId())
					.orElseThrow(() -> new UserNotFoundByIdException("given id is present in database"));
			if ((!classHourRepo.existsByRoomNoAndBeginsAtBetween(req.getRoomNo(),
					classHour.getBeginsAt().minusMinutes(1), classHour.getEndsAt().plusMinutes(1)))) {
				if (!(user.getUserRole().equals(UserRole.TEACHER))) {
					classHour.setRoomNo(roomNo);
					classHour.setSubject(subject);
					classHour.setUser(user);
				} else {
					throw new AlreadyClassHourAssoatedException("class already assoated");
				}
			} else {
				throw new UserNotFoundByIdException("Invalid room id");
			}
		});
		structure.setData(classHourRequest);
		structure.setMessage("class hour created");
		structure.setStatus(HttpStatus.CREATED.value());

		return new ResponseEntity<ResponseStructure<ClassHourRequest>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHour>>> createClassHoursForNextweek(int programId) {

		 AcademicProgram academicProgram = academicProgramRepo.findById(programId).get();
		 List<ClassHour> classHours = academicProgram.getClassHours();
		 List<ClassHour> classHours2=new ArrayList<ClassHour>();
		 classHours.forEach((hour)->{
			 ClassHour newClassHour = createNewClassHour(hour);
			 classHours2.add(newClassHour);
		 });
		 classHours2.forEach((hour)->{
			 LocalDateTime plusDays = hour.getBeginsAt().plusDays(7);
			 hour.setBeginsAt(plusDays);
			 ClassHour save = classHourRepo.save(hour);
		 });
		    structure.setData(classHourRequest);
			structure.setMessage("New Classhour created for next week");
			structure.setStatus(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<List<ClassHour>>>(HttpStatus.CREATED);
	}
	public ClassHour createNewClassHour(ClassHour classHour) {
		ClassHour hour = new ClassHour();
		hour.setAcademicProgram(classHour.getAcademicProgram());
		hour.setBeginsAt(classHour.getBeginsAt());
		hour.setClassStatus(classHour.getClassStatus());
		hour.setEndsAt(classHour.getEndsAt());
		hour.setRoomNo(classHour.getRoomNo());
		hour.setSubject(classHour.getSubject());
		hour.setUser(classHour.getUser());
		return hour;

	}

	@Override
	public ResponseEntity<ResponseStructure<ClassHourRequest>> deleteClassHour(List<ClassHour> classHours) {
//		classHourRepo.delete(classHours);
		return null;
	}
}
