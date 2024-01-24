package com.school.sba.requestdto;

import org.springframework.stereotype.Component;

import com.school.sba.enums.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
	@NotNull(message = "userName Should not be empty")
	private String userName;
	@NotNull(message = "password Should not be empty")
	private String password;
	@NotNull(message = "FirstName Should not be empty")
	private String firstName;
	@NotNull(message = "LastName Should not be empty")
	private String lastName;
	@NotNull(message = "ContactNo Should not be empty")
	private long contactNo;
	@NotNull(message = "Email Should not be empty")
	private String email;
	private UserRole userRole;
}
