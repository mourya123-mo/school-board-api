package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.ClassHour;
import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.util.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<ClassHour>> createClassHour(int programId);

	ResponseEntity<ResponseStructure<ClassHourRequest>> updateClassHour(List<ClassHourRequest> updateRequest);

	ResponseEntity<ResponseStructure<ClassHourRequest>> deleteClassHour(List<ClassHour> classHours);

	ResponseEntity<ResponseStructure<List<ClassHour>>> createClassHoursForNextweek(int programId);

}
