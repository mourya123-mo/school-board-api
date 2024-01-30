package com.school.sba.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.school.sba.exception.AlreadyClassHourAssoatedException;
import com.school.sba.exception.ConstraintVoilationException;
import com.school.sba.exception.DuplicateEntryException;
import com.school.sba.exception.ScheduleNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByIdException;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	private ResponseEntity<Object> structure(HttpStatus status, String message, Object rootCause) {
		return new ResponseEntity<Object>(Map.of("status", status.value(), "message", message, "rootCause", rootCause

		), status);

	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<ObjectError> allErrors = ex.getAllErrors();
		Map<String, String> errors = new HashMap<String, String>();
		allErrors.forEach(error -> {
			FieldError fieldError = (FieldError) error;
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		});
		return structure(HttpStatus.BAD_REQUEST, "failed to save data", errors);

	}

	@ExceptionHandler(ConstraintVoilationException.class)
	public ResponseEntity<Object> handleConstraintVailationException(ConstraintVoilationException ex) {
		return new ResponseEntity<Object>(structure(HttpStatus.ALREADY_REPORTED, ex.getMessage(), "school already created"),HttpStatus.ALREADY_REPORTED);

	}

	@ExceptionHandler(DuplicateEntryException.class)
	public ResponseEntity<Object> handleDuplicateEntityException(DuplicateEntryException ex) {
		return new ResponseEntity<Object>(structure(HttpStatus.NOT_ACCEPTABLE,ex.getMessage(),"duplicate entry of username"),HttpStatus.NOT_ACCEPTABLE);

	}
	@ExceptionHandler(UserNotFoundByIdException.class)
	public ResponseEntity<Object> handleUserNotFoundByIdException(UserNotFoundByIdException ex) {
		return new ResponseEntity<Object>( structure(HttpStatus.NOT_FOUND, ex.getMessage(), "user not found with given id"),HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler(ScheduleNotFoundByIdException.class)
	public ResponseEntity<Object> handleScheduleNotFoundByIdException(ScheduleNotFoundByIdException ex){
		return new ResponseEntity<Object>(structure(HttpStatus.NOT_FOUND, ex.getMessage(), "schedule not found by given id"),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AlreadyClassHourAssoatedException.class)
	public ResponseEntity<Object> handleAlreadyClassHourAssoatedException(AlreadyClassHourAssoatedException ex){
		return new ResponseEntity<Object>(structure(HttpStatus.ALREADY_REPORTED, ex.getMessage(), "already classhour assoated"),HttpStatus.ALREADY_REPORTED);
	}

			

}
