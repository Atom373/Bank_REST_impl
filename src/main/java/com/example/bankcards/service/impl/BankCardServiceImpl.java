package com.example.bankcards.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.CardNotFoudException;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.service.BankCardService;
import com.example.bankcards.util.PanEncryptor;
import com.example.bankcards.util.BankCardUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankCardServiceImpl implements BankCardService {
	
	private static final short CARD_LIFETIME = 4; // 4 years 

	private final BankCardRepository cardRepository;
	private final BankCardUtils cardUtils;
	private final PanEncryptor cardEncryptor;
	
	@Override
	public List<BankCard> getAll() {
		return cardRepository.findAll();
	}
	
	@Override
	public List<BankCard> getAllByOwnerId(Long id) {
		return cardRepository.findAllByOwnerId(id);
	}
	
	@Override
	public BankCard save(BankCard card) {
		String pan = cardUtils.generatePan();
		
		card.setMaskedPan(cardUtils.getMaskedPan(pan));
		card.setEncryptedPan(cardEncryptor.encrypt(pan));
		card.setExpirationDate(LocalDate.now().plusYears(CARD_LIFETIME));
		card.setStatus(CardStatus.ACTIVE);
		
		log.debug(card.toString());
		
		return cardRepository.save(card);
	}

	@Override
	public void updateStatus(Long cardId, CardStatus status) {
		BankCard card = cardRepository.findById(cardId)
				.orElseThrow(
						() -> new CardNotFoudException("Card with id = %s not found".formatted(cardId))
				);
		card.setStatus(status);
		cardRepository.save(card);
	}

	@Override
	public void deleteById(Long id) {
		cardRepository.deleteById(id);
	}

}
