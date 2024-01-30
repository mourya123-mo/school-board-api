package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.enums.UserRole;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.util.ResponseStructure;

public interface AcademicProgramService {

	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> saveAcademicProgram(AcademicProgramRequest req,
			int schoolId);

	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> fetchAllAcademicProgram(int schoolId);

	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcadamicProgram(int programId, int userId);

	public ResponseEntity<ResponseStructure<List<UserResponse>>> fetchUser(UserRole userRole, int programId);

	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteById(int programId);

}