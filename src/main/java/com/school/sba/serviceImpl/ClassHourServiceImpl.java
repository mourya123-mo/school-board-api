package com.school.sba.serviceImpl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
				for (int day = 1; day <= 6; day++) {
					for (int hour = 0; hour < classHourPerDay + 2; hour++) {
						ClassHour classHour = new ClassHour();
						if (!currentTime.toLocalTime().equals(lunchTimeStart) && !isLunchTime(currentTime, schedule)) {
							if (!currentTime.toLocalTime().equals(breakTimeStart)
									&& !isBreakTime(currentTime, schedule)) {
								LocalDateTime beginsAt = currentTime;
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
		User user=	userRepo.findById(req.getUserId())
					.orElseThrow(() -> new UserNotFoundByIdException("given id not presnt in data base"));
			Subject subject = subjectRepo.findById(req.getSubjectId())
					.orElseThrow(() -> new UserNotFoundByIdException("given id not presnt in data base"));
			int roomNo = req.getRoomNo();
			ClassHour classHour = classHourRepo.findById(req.getClassHourId())
					.orElseThrow(() -> new UserNotFoundByIdException("given id is present in database"));
			if((!classHourRepo.existsByRoomNoAndBeginsAtBetween(req.getRoomNo(), classHour.getBeginsAt().minusMinutes(1), classHour.getEndsAt().plusMinutes(1)))) {
				if(!(user.getUserRole().equals(UserRole.TEACHER))) {
					classHour.setRoomNo(roomNo);
					classHour.setSubject(subject);
					classHour.setUser(user);
				}else {
					throw new AlreadyClassHourAssoatedException("class already assoated");
				}
			}else {
				throw new UserNotFoundByIdException("Invalid room id");
			}
		});
		structure.setData(classHourRequest);
		structure.setMessage("class hour created");
		structure.setStatus(HttpStatus.CREATED.value());
		
		
		return new ResponseEntity<ResponseStructure<ClassHourRequest>>(structure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<ClassHourRequest>> deleteClassHour(List<ClassHour> classHours) {
//		classHourRepo.delete(classHours);
		return null;
	}

}
