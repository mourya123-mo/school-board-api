package com.school.sba.entity;

import java.time.Duration;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scheduleId;
	private LocalTime opeansAt;
	private LocalTime closesAt;
	private int classHoursPerDay;
	private Duration classHoursLengthInMinutes;
	private LocalTime breakTime;
	private Duration breakLengthInMinutes;
	private LocalTime lunchTime;
	private Duration lunchLengthInMinutes;
	@OneToOne
	private School school;
	
	
	

}
