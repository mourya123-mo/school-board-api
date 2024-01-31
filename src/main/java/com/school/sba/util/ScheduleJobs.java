package com.school.sba.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.serviceImpl.AcademicProgramServiceImpl;

@Component
public class ScheduleJobs {
	@Autowired
	private AcademicProgramServiceImpl academicProgramServiceImpl;

	ScheduleJobs(){
		System.out.println("schedule job class created");
	}
	@Scheduled(fixedDelay = 1000l)
	public void test() {
		
		System.out.println("schedule job");
		System.out.println(academicProgramServiceImpl.perminentDelete());
	}
}
