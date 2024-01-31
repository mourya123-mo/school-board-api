package com.school.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.AcademicProgram;
import java.util.List;


public interface AcademicProgramRepo extends JpaRepository<AcademicProgram, Integer> {
	
 List<AcademicProgram> findByIsDeleted(boolean deleted);

}
