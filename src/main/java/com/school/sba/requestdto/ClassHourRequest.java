package com.school.sba.requestdto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ClassHourRequest {
	private int subjectId;
	private int userId;
	private int roomNo;
	private int classHourId;
}
