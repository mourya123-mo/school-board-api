package com.school.sba.util;

import org.springframework.stereotype.Component;

import com.school.sba.entity.User;
import com.school.sba.responsedto.SchoolResponse;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ResponseStructure <T>{

	private int status;
	private String message;
	private T data;
}
