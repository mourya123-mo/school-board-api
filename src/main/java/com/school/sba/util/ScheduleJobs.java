package com.school.sba.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.school.sba.serviceImpl.AcademicProgramServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduleJobs {
	@Autowired
	private AcademicProgramServiceImpl academicProgramServiceImpl;


//	@Scheduled(fixedDelay = 1000l)
//	public void test() {
//		
//		System.out.println("schedule job");
//		System.out.println(academicProgramServiceImpl.perminentDelete());
//	}
//	<minute> <hour> <day-of-month> <month> <day-of-week> <command>
//	@Scheduled(cron = "* * * * MON")
//	public void generateClasshourEveryMonday() {
//		
//	}
	
}
