package com.school.sba.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.ConstraintVoilationException;
import com.school.sba.exception.UserNotFoundByIdException;
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

	private School mapToSchool(SchoolRequest schoolrequest) {
		return School.builder().schoolName(schoolrequest.getSchoolName()).Adress(schoolrequest.getAdress())
				.emailId(schoolrequest.getEmailId()).contactNo(schoolrequest.getContactNo()).build();

	}

	private SchoolResponse mapToSchoolResponse(School school) {

		return SchoolResponse.builder().schoolId(school.getSchoolId()).schoolName(school.getSchoolName())
				.Adress(school.getAdress()).emailId(school.getEmailId()).contactNo(school.getContactNo()).build();

	}

	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(int userId, SchoolRequest schoolRequest) {
		return userRepo.findById(userId).map(u->{
			if(u.getUserRole().equals(UserRole.ADMIN)) {
				if(u.getSchool()==null) {
					School school = mapToSchool(schoolRequest);
					school=schoolRepo.save(school);//saved the new school
					u.setSchool(school);// update user with new school
					userRepo.save(u);
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("school saved sucessfully");
					structure.setData(mapToSchoolResponse(school));
					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);
				}else 
					throw new UserNotFoundByIdException("user id not present in database");
				
			}else
				throw new ConstraintVoilationException("only admin can create school ");
		}).orElseThrow(()-> new UserNotFoundByIdException("failed to save school") );

}
}
