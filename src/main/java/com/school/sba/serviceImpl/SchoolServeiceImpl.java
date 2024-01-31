package com.school.sba.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.ConstraintVoilationException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.ClassHourRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.util.ResponseStructure;

@Service
public class SchoolServeiceImpl implements SchoolService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private SchoolRepo schoolRepo;
	@Autowired
	ResponseStructure<SchoolResponse> structure;
	@Autowired
	private ClassHourRepo classHourRepo;
	@Autowired
	private AcademicProgramRepo academicProgramRepo;

	private School mapToSchool(SchoolRequest schoolrequest) {
		return School.builder().schoolName(schoolrequest.getSchoolName()).Adress(schoolrequest.getAdress())
				.emailId(schoolrequest.getEmailId()).contactNo(schoolrequest.getContactNo()).build();

	}

	private SchoolResponse mapToSchoolResponse(School school) {

		return SchoolResponse.builder().schoolId(school.getSchoolId()).schoolName(school.getSchoolName())
				.Adress(school.getAdress()).emailId(school.getEmailId()).contactNo(school.getContactNo()).build();

	}

	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(SchoolRequest schoolRequest) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByUserName(userName).map(u -> {
			if (u.getUserRole().equals(UserRole.ADMIN)) {
				if (u.getSchool() == null) {
					School school = mapToSchool(schoolRequest);
					school = schoolRepo.save(school);// saved the new school
					u.setSchool(school);// update user with new school
					userRepo.save(u);
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("school saved sucessfully");
					structure.setData(mapToSchoolResponse(school));
					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure, HttpStatus.CREATED);
				} else
					throw new UserNotFoundByIdException("user id not present in database");

			} else
				throw new ConstraintVoilationException("only admin can create school ");
		}).orElseThrow(() -> new UserNotFoundByIdException("failed to save school"));

	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteById(int schoolId) {
	School school = schoolRepo.findById(schoolId).orElseThrow(()-> new UserNotFoundByIdException("school with given id is not present"));
		if(school.isDeleted()==false) {
			school.setDeleted(true);
			schoolRepo.save(school);
		}
		structure.setStatus(HttpStatus.OK.value());
		structure.setMessage("softdeleted sucessfull");
		structure.setData(mapToSchoolResponse(school));
		
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
	}
	public String perminentDelete() {
		List<School> schools = schoolRepo.findByIsDeleted(true);
		schools.forEach(school->{
			List<AcademicProgram> programs = school.getAcademicPrograms();
			programs.forEach(program->{
				List<ClassHour> classHours = program.getClassHours();
				classHourRepo.deleteAll(classHours);
			});
			academicProgramRepo.deleteAll(programs);
			List<User> users = userRepo.findBySchool(school);
			users.forEach(user->{
				if(user.getUserRole().equals(UserRole.ADMIN)) {
					users.remove(user);
				}
			});
			userRepo.deleteAll(users);
			schoolRepo.deleteAll(schools);
		});
		return "school deleted";
		
	}
}
