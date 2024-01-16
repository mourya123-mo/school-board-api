package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.User;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.util.ResponseStructure;

public interface UserService  {

	ResponseEntity<ResponseStructure<UserResponse>> regesterUser(UserRequest userRequest);

}
