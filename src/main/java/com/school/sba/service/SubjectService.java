package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.util.ResponseStructure;

public interface SubjectService {

	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(SubjectRequest subjectRequest,
			@PathVariable int programId);

	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubjects(
			@RequestBody SubjectRequest subjectRequest, @PathVariable int programId);

	public ResponseEntity<ResponseStructure<List<Subject>>> fetchAllSubjects();

	public ResponseEntity<ResponseStructure<User>> addSubjectToTeacher(int subjectId, int userId);

}
