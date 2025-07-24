package com.example.bankcards.shceduling.task;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExpiredCardScheduler {

	private final CardRepository bankCardRepository;
	
	@Scheduled(cron = "0 0 00 * * ?")
	public void findAndMarkExpiredCards() {
		LocalDate currentDate = LocalDate.now();
		
		List<Card> cards = bankCardRepository.findAll();
		
		List<Card> expiredCards = new LinkedList<>();
		
		for (Card card : cards) {
			if (card.getExpirationDate().isBefore(currentDate)) {
				card.setStatus(CardStatus.EXPIRED);
				expiredCards.add(card);
			}
		}
		bankCardRepository.saveAll(expiredCards);
	}
}
