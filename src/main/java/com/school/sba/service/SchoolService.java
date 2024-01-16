package com.school.sba.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.repository.SchoolRepo;

@Service
public class SchoolService {

	School school = new School();
	@Autowired
	private SchoolRepo repo;

	public void saveSchool(String schoolName, long contactNo, String emailId, String Adress) {

		school.setSchoolName(schoolName);
		school.setContactNo(contactNo);
		school.setEmailId(emailId);
		school.setAdress(Adress);
		repo.save(school);
	}

	public List<School> fetchAll() {

		List<School> school = repo.findAll();
		for (School s : school) {
			System.out.println(s.getSchoolName() + " " + s.getContactNo() + " " + s.getAdress() + " " + s.getEmailId());
		}
		return school;
	}

	public School getSchoolById(int schoolId) {

		School school2 = repo.findById(schoolId).get();
		return school2;

	}

	public String updateSchoolById(int schoolId, String schoolName, long contactNo, String emailId, String adress) {

		School schoolById = getSchoolById(schoolId);
		schoolById.setSchoolName(schoolName);
		schoolById.setContactNo(contactNo);
		schoolById.setEmailId(emailId);
		schoolById.setAdress(adress);
		return "schoolId" + schoolById.getSchoolId() + "updated";

	}

	public String deleteSchoolById(int schoolId) {
		repo.deleteById(schoolId);
		return schoolId + " deleted";

	}

}
