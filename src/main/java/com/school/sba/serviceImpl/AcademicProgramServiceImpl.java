package com.school.sba.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.ConstraintVoilationException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.SchoolRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

@Service
public class AcademicProgramServiceImpl implements AcademicProgramService {

	@Autowired
	private AcademicProgramRepo programRepo;

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;

	@Autowired
	private ResponseStructure<List<AcademicProgramResponse>> responseStructure;

	@Autowired
	private ResponseStructure<List<UserResponse>> userStructure;

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveAcademicProgram(
			AcademicProgramRequest programRequest, int schoolId) {
		AcademicProgram program = programRepo.save(mapToAcademicProgram(programRequest));
		School school = schoolRepo.findById(schoolId).get();
		school.getAcademicPrograms().add(program);
		program.setSchool(school);
		schoolRepo.save(school);
		structure.setData(mapToAcademicResponseProgram(program));
		structure.setMessage("Academic Program saved to the database");
		structure.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> fetchAllAcademicProgram(int schoolId) {
		School school = schoolRepo.findById(schoolId).get();
		List<AcademicProgram> listAcademicProgram = school.getAcademicPrograms();
		List<AcademicProgramResponse> responses = new ArrayList<>();
		for (AcademicProgram academicProgram : listAcademicProgram) {
			responses.add(mapToAcademicResponseProgram(academicProgram));
		}
		responseStructure.setData(responses);
		responseStructure.setMessage("Academic Program saved to the database");
		responseStructure.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.CREATED);
	}

	public AcademicProgram mapToAcademicProgram(AcademicProgramRequest programRequest) {
		return AcademicProgram.builder().programType(programRequest.getProgramType())
				.programName(programRequest.getProgramName()).beginsAt(programRequest.getBeginsAt())
				.endsAt(programRequest.getEndsAt()).build();
	}

	public AcademicProgramResponse mapToAcademicResponseProgram(AcademicProgram academicProgram) {
		return AcademicProgramResponse.builder().programId(academicProgram.getProgramId())
				.programType(academicProgram.getProgramType()).programNameString(academicProgram.getProgramName())
				.beginsAt(academicProgram.getBeginsAt()).endsAt(academicProgram.getEndsAt()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcadamicProgram(int programId, int userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("user not present in database"));
		AcademicProgram academicProgram = programRepo.findById(programId)
				.orElseThrow(() -> new UserNotFoundByIdException("academic program not present in database"));
		if (user.getUserRole().equals(UserRole.ADMIN)) {
			throw new ConstraintVoilationException("admin cannot be added to acadenic program");
		} else {
			if (academicProgram.getSubjects().contains(user.getSubject())) {

				academicProgram.getUsers().add(user);
				programRepo.save(academicProgram);
				structure.setMessage("users added to academic program");
				structure.setStatus(HttpStatus.CREATED.value());
				structure.setData(mapToAcademicResponseProgram(academicProgram));
			} else {
				throw new ConstraintVoilationException("no subject assoated to academic program");
			}
		}
		return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponse>>> fetchUser(UserRole userRole, int programId) {
		AcademicProgram academicProgram = programRepo.findById(programId)
				.orElseThrow(() -> new UserNotFoundByIdException("id not found in database"));
		List<User> role = userRepo.findByUserRoleAndAcademicPrograms(userRole, academicProgram);
		List<UserResponse> userResponses = new ArrayList();
		role.forEach(users -> {
			userResponses.add(userServiceImpl.mapToUserResponse(users));
		});

		userStructure.setMessage("sucessfully fetched");
		userStructure.setStatus(HttpStatus.FOUND.value());
		userStructure.setData(userResponses);
		return new ResponseEntity<ResponseStructure<List<UserResponse>>>(userStructure, HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteById(int programId) {
		AcademicProgram program = programRepo.findById(programId).orElseThrow(()-> new UserNotFoundByIdException("given id is not present in database"));
		if(program.isDeleted()==false) {
			program.setDeleted(true);
			programRepo.save(program);
		}
		structure.setData(mapToAcademicResponseProgram(program));
		structure.setMessage("softdelete sucessful");
		structure.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.OK);
	}

}
