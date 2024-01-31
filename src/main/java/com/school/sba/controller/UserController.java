package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.enums.UserRole;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

import jakarta.validation.Valid;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/users")
	@PreAuthorize(value = "hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(@RequestBody @Valid UserRequest userRequest) {

		return userService.addOtherUser(userRequest);
	}

	@PostMapping("/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> regesterAdmin(@RequestBody @Valid UserRequest userRequest) {

		return userService.regesterAdmin(userRequest);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> getUserById(@PathVariable int userId)
			throws UserNotFoundByIdException {
		return userService.getUserById(userId);
	}

	// soft delete
	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUserById(@PathVariable int userId){
		
		return userService.deleteUserById(userId);
	}

	
	
	
	
}


