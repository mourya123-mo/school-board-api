package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.util.ResponseStructure;

@Controller
public class SchoolController {
	@Autowired
	private SchoolService schoolservice;

	@PostMapping("/user/{userId}/schools")
public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool( @PathVariable int userId ,@RequestBody SchoolRequest schoolRequest){
	
		return schoolservice.createSchool(userId, schoolRequest);
}

	

}
