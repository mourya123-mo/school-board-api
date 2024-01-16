package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@AllArgsConstructor
@NoArgsConstructor
public class DuplicateEntryException extends RuntimeException {

	private int status;
	private String message;
	private String rootcause;
}
