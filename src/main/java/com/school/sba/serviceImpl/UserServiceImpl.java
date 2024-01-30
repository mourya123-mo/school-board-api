package com.school.sba.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.DuplicateEntryException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepo;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ResponseStructure<UserResponse> structure;
	
	@Autowired
	private AcademicProgramRepo academicProgramRepo;
	

	private User mapToUserRequest(UserRequest request) {
		return User.builder().userName(request.getUserName()).password(encoder.encode(request.getPassword()))
				.firstName(request.getFirstName()).lastName(request.getLastName()).contactNo(request.getContactNo())
				.email(request.getEmail()).userRole(request.getUserRole()).build();

	}

	public UserResponse mapToUserResponse(User user) {

		return UserResponse.builder().userId(user.getUserId()).userName(user.getUserName())
				.firstName(user.getFirstName()).lastName(user.getLastName()).contactNo(user.getContactNo())
				.email(user.getEmail()).userRole(user.getUserRole()).build();
	}

	/*----------------------------->To regester user <-----------------------------*/
	int count = 0;

	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(UserRequest userRequest) {
		User user = mapToUserRequest(userRequest);
		user.setDeleted(false);
		User user1 = userRepo.findByUserRole(UserRole.ADMIN)
				.orElseThrow(() -> new UserNotFoundByIdException("admin role not found"));

		if (user.getUserRole() != UserRole.ADMIN) {
			School school = user1.getSchool();
			user.setSchool(school);
			userRepo.save(user);
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("data saved sucessful");
			structure.setData(mapToUserResponse(user));
		} else {
			throw new DuplicateEntryException("there can only be one admin");

		}
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.CREATED);
	}

	/*------------------------------------->To get user by Id <-----------------------------*/

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> getUserById(int userId) throws UserNotFoundByIdException {
		User user = new User();
		try {
			user = userRepo.findById(userId).get();

		} catch (Exception e) {

			throw new UserNotFoundByIdException("user not present in database");
		}
		structure.setStatus(HttpStatus.FOUND.value());
		structure.setMessage("user with given id found");
		structure.setData(mapToUserResponse(user));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.FOUND);
	}

	/*------------------------>soft delete <--------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUserById(int userId) {

		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("user not present in database"));
		if (user.isDeleted() == false)
			user.setDeleted(true);
		User user2 = userRepo.save(user);
		structure.setStatus(HttpStatus.OK.value());
		structure.setMessage("deleted status updated sucessfully");
		structure.setData(mapToUserResponse(user2));

		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> regesterAdmin(@Valid UserRequest userRequest) {
		User user = mapToUserRequest(userRequest);
		user.setDeleted(false);
		boolean role = userRepo.existsByUserRole(UserRole.ADMIN);
		if (role == false && user.getUserRole() != UserRole.STUDENT && user.getUserRole() != UserRole.TEACHER) {

			userRepo.save(user);

			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("admin saved sucessfully");
			structure.setData(mapToUserResponse(user));
		} else {
			throw new DuplicateEntryException("only admin role can be registered");
		}
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.OK);
	}

	

	
}
