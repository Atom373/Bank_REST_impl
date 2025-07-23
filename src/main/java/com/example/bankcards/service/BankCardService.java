package com.example.bankcards.service;

import java.util.List;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.enums.CardStatus;

public interface BankCardService {

	List<BankCard> getAll();
	
	List<BankCard> getAllByOwnerId(Long id); 
	
	BankCard getById(Long Id);
	
	BankCard save(BankCard card);
	
	void updateStatus(Long cardId, CardStatus status);
	
	void deleteById(Long id);
}
