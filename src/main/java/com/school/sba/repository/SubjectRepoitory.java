package com.school.sba.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.Subject;

public interface SubjectRepoitory extends JpaRepository<Subject, Integer> {
	Optional<Subject> findBySubjectName(String subjectNames);
}
