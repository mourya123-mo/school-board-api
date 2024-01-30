package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.enums.UserRole;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.util.ResponseStructure;

@RestController
public class AcadamicProgramController {

	@Autowired
	AcademicProgramService programService;

	@PostMapping(path = "/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveAcademicProgram(@PathVariable int schoolId,
			@RequestBody AcademicProgramRequest req) {
		return programService.saveAcademicProgram(req, schoolId);
	}

	@GetMapping(path = "/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> fetchAllAcademicProgram(
			@PathVariable int schoolId) {
		return programService.fetchAllAcademicProgram(schoolId);
	}
	@PutMapping("/academic-programs/{programId}/users/{userId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcadamicProgram(@PathVariable int programId,@PathVariable 
			int userId){
		
		return programService.addAcadamicProgram( programId, userId);
	}
	@GetMapping(path="/academic-programs/{programId}/user-roles/{role}/users")
	public ResponseEntity<ResponseStructure<List<UserResponse>>> fetchUsers(@PathVariable UserRole role ,@PathVariable int programId ){
		return programService.fetchUser(role, programId);
	}
	@DeleteMapping(path="/academic-programs/{programId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteById(@PathVariable int programId){
		return programService.deleteById(programId);
	}
}
