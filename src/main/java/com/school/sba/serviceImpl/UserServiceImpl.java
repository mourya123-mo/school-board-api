package com.school.sba.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.User;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.ConstraintVoilationException;
import com.school.sba.exception.DuplicateEntryException;
import com.school.sba.repository.UserRepo;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ResponseStructure<UserResponse> structure;

	private User mapToUserRequest(UserRequest request) {
		return User.builder().userName(request.getUserName()).password(request.getPassword())
				.firstName(request.getFirstName()).lastName(request.getLastName()).contactNo(request.getContactNo())
				.email(request.getEmail()).userRole(request.getUserRole()).build();

	}

	private UserResponse mapToUserResponse(User user) {

		return UserResponse.builder().userId(user.getUserId()).userName(user.getUserName())
				.firstName(user.getFirstName()).lastName(user.getLastName()).contactNo(user.getContactNo())
				.email(user.getEmail()).userRole(user.getUserRole()).build();
	}

	int count = 0;

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> regesterUser(UserRequest userRequest) {
		User user = mapToUserRequest(userRequest);
		if (userRequest.getUserRole() == UserRole.ADMIN) {
			count++;
		}
		if (count == 1 || userRequest.getUserRole() != UserRole.ADMIN) {

			try {
				user = userRepo.save(user);
			} catch (Exception e) {
				throw new DuplicateEntryException(HttpStatus.ALREADY_REPORTED.value(), "Duplicate entry",
						"please check user details entered");
			}
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("data saved sucessful");
			structure.setData(mapToUserResponse(user));

		} else {
			throw new DuplicateEntryException(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(),
					"there can only be admin", "please check admin details");
		}
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.CREATED);
	}

}
