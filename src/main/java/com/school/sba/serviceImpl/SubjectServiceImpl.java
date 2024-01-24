package com.school.sba.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.ConstraintVoilationException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.SubjectRepoitory;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.util.ResponseStructure;

@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepoitory subjectRepo;
	@Autowired
	private AcademicProgramRepo acdemicRepo;
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	@Autowired
	private AcademicProgramServiceImpl academicServiceImpl;
	@Autowired
	private ResponseStructure<List<Subject>> subjectStructure;
	@Autowired
	private ResponseStructure<User> userStructure;
	@Autowired
	private UserRepo userRepo;

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(SubjectRequest subjectRequest,
			int programId) {

		return acdemicRepo.findById(programId).map(program -> {// found acdemic program
			List<Subject> subjects = new ArrayList<Subject>();
			subjectRequest.getSubjectNames().forEach(name -> {// iterating over each subject name
				// finding subject based on name in current ittaration
				Subject subject = subjectRepo.findBySubjectName(name).map(s -> s) // add existing subject to the
																					// subjectlist
						.orElseGet(() -> {// if not found create new subject
							Subject subject2 = new Subject();
							subject2.setSubjectName(name);
							subjectRepo.save(subject2);

							return subject2;
						});
				subjects.add(subject);
			});
			program.setSubjects(subjects);
			acdemicRepo.save(program);
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("updated subject list to acdemicProgram");
			structure.setData(academicServiceImpl.mapToAcademicResponseProgram(program));
			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.CREATED);
		}).orElseThrow(() -> new ConstraintVoilationException("user not found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubjects(SubjectRequest subjectRequest,
			int programId) {

		return acdemicRepo.findById(programId).map(program -> {// found acdemic program
			List<Subject> subjects = new ArrayList<Subject>();
			subjectRequest.getSubjectNames().forEach(name -> {// iterating over each subject name
				// finding subject based on name in current ittaration
				Subject subject = subjectRepo.findBySubjectName(name).map(s -> s) // add existing subject to the
																					// subjectlist
						.orElseGet(() -> {// if not found create new subject
							Subject subject2 = new Subject();
							subject2.setSubjectName(name);
							subjectRepo.save(subject2);

							return subject2;
						});
				subjects.add(subject);
			});
			program.setSubjects(subjects);
			acdemicRepo.save(program);
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("updated subject list to acdemicProgram");
			structure.setData(academicServiceImpl.mapToAcademicResponseProgram(program));
			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure, HttpStatus.CREATED);
		}).orElseThrow(() -> new ConstraintVoilationException("user not found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Subject>>> fetchAllSubjects() {
		List<Subject> subjectList = subjectRepo.findAll();
		subjectStructure.setStatus(HttpStatus.FOUND.value());
		subjectStructure.setMessage("subject found sucessfully");
		subjectStructure.setData(subjectList);
		
		
		
		return new ResponseEntity<ResponseStructure<List<Subject>>>(subjectStructure,HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<User>> addSubjectToTeacher(int subjectId, int userId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(()-> new UserNotFoundByIdException("id not present in database"));
	User user=	userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("user with given id is not found"));
	if(user.getUserRole().equals(UserRole.TEACHER)) {
		user.setSubject(subject);
		userRepo.save(user);
		userStructure.setMessage("subject updated to user");
		userStructure.setStatus(HttpStatus.OK.value());
		userStructure.setData(user);
	}else {
		throw new ConstraintVoilationException("subject cannot set to user");
	}
		return new ResponseEntity<ResponseStructure<User>>(userStructure,HttpStatus.OK) ;
	}
	

}