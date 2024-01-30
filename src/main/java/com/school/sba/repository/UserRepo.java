package com.school.sba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;

public interface UserRepo extends JpaRepository<User, Integer> {

	boolean existsByUserRole(UserRole userRole);

	Optional<User> findByUserName(String userName);
	
    Optional<User> findByUserRole(UserRole userRole);
    
    List<User> findByUserRoleAndAcademicPrograms(UserRole userRole, AcademicProgram academicProgram);
}
