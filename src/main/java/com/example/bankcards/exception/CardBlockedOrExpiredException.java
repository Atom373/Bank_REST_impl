package com.example.bankcards.exception;

public class CardBlockedOrExpiredException extends AppException {

	public CardBlockedOrExpiredException(String msg) {
		super(msg);
	}
}
