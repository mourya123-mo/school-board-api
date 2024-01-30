package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.util.ResponseStructure;

@RestController
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	@PostMapping("/academic-program/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubjects(
			@RequestBody SubjectRequest subjectRequest, @PathVariable int programId) {

		return subjectService.addSubject(subjectRequest, programId);
	}
	@PutMapping("/academic-program/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubjects(
			@RequestBody SubjectRequest subjectRequest, @PathVariable int programId) {
		
		return subjectService.updateSubjects(subjectRequest, programId);
	}
	@GetMapping("/subjects")
	public ResponseEntity<ResponseStructure<List<Subject>>> fetchAllSubjects(){
		return subjectService.fetchAllSubjects();
	}
	@PutMapping("/subjects/{subjectId}/users/{userId}")
	public ResponseEntity<ResponseStructure<User>> addSubjectToTeacher(@PathVariable int subjectId,
	@PathVariable	int userId){
		
		return subjectService.addSubjectToTeacher(subjectId,userId);
	}

	
}
