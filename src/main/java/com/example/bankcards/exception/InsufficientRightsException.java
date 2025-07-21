package com.example.bankcards.exception;

public class InsufficientRightsException extends RuntimeException {

	public InsufficientRightsException(String msg) {
		super(msg);
	}
}