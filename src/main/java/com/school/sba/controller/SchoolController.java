package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.school.sba.entity.School;
import com.school.sba.service.SchoolService;

@Controller
public class SchoolController {
	@Autowired
	private SchoolService service;

	public void saveSchool(String schoolName, long contactNo, String emailId, String Adress) {

		service.saveSchool(schoolName, contactNo, emailId, Adress);

	}

	public List<School> fetchAll() {

		return service.fetchAll();
	}

	public void getSchoolById(int schoolId) {

		service.getSchoolById(schoolId);
	}

	public String updateSchoolBtId(int schoolId, String schoolName, long contactNo, String emailId, String Adress) {
		return service.updateSchoolById(schoolId, schoolName, contactNo, emailId, Adress);
	}

	public String deleteSchoolById(int SchoolId) {
		return service.deleteSchoolById(SchoolId);
	}

}
