package com.school.sba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchoolBoardApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolBoardApiApplication.class, args);
		System.out.println("start");
	}

}
