package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.User;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.util.ResponseStructure;

public interface UserService  {

	ResponseEntity<ResponseStructure<UserResponse>> regesterUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> getUserById(int userId) throws UserNotFoundByIdException;

	ResponseEntity<ResponseStructure<UserResponse>> deleteUserById(int userId) throws UserNotFoundByIdException;

}
