package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@PostMapping("/schools")
public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool( @RequestBody SchoolRequest schoolRequest){
	
		return schoolservice.createSchool( schoolRequest);
}

	@DeleteMapping(path="/school/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteByID(@PathVariable int schoolId){
		return schoolservice.deleteById(schoolId);
	}

}
