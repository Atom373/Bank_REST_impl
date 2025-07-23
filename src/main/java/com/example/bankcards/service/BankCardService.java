package com.example.bankcards.service;

import java.util.List;

import com.example.bankcards.controller.payload.TransactionRequest;
import com.example.bankcards.dto.RevealedCardInfoDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.enums.CardStatus;

public interface BankCardService {

	List<BankCard> getAll();
	
	List<BankCard> getAllByOwnerId(Long id); 
	
	BankCard getById(Long cardId, Long userId);
	
	RevealedCardInfoDto revealCardInfo(Long id, Long userId, String password);
	
	BankCard save(BankCard card);
	
	void updateStatus(Long cardId, CardStatus status);
	
	void transfer(TransactionRequest request, Long userId);
	
	void deleteById(Long id);
}
