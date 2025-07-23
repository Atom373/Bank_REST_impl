package com.example.bankcards.exception.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.bankcards.exception.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AppException.class)
	public ResponseEntity<?> defaultHandler(AppException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				Map.of(
						"status_code", 400,
						"error_message", ex.getMessage(),
						"timestamp", LocalDateTime.now()
				)
		);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> invalidQueryParameter(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				Map.of(
						"status_code", 400,
						"error_message", "Invalid query parameter value",
						"timestamp", LocalDateTime.now()
				)
		);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> validationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
