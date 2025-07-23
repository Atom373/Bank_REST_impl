package com.example.bankcards.exception;

public class UserAlreadyExistsException extends AppException {
	
	public UserAlreadyExistsException(String msg) {
		super(msg);
	}
}
