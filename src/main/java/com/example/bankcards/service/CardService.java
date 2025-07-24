package com.example.bankcards.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.bankcards.controller.payload.TransactionRequest;
import com.example.bankcards.dto.RevealedCardInfoDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;

public interface CardService {

	List<Card> getAll();
	
	List<Card> getAllByOwnerId(Long id); 
	
	Page<Card> getAllByOwnerId(Long id, Pageable pageable); 
	
	Card getById(Long cardId, Long userId);
	
	RevealedCardInfoDto revealCardInfo(Long id, Long userId, String password);
	
	Card save(Card card);
	
	void updateStatus(Long cardId, CardStatus status);
	
	void transfer(TransactionRequest request, Long userId);
	
	void deleteById(Long id);
}
