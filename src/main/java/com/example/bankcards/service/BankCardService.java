package com.example.bankcards.service;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.enums.CardStatus;

public interface BankCardService {

	BankCard save(BankCard card);
	
	void updateStatus(BankCard card, CardStatus status);
	
	void deleteById(Long id);
}
