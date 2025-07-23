package com.example.bankcards.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bankcards.dto.RevealedCardInfoDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.CardNotFoudException;
import com.example.bankcards.exception.InsufficientRightsException;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.service.BankCardService;
import com.example.bankcards.util.PanEncryptor;
import com.example.bankcards.util.SecurityUtils;
import com.example.bankcards.util.BankCardUtils;
import com.example.bankcards.util.DateFormatUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankCardServiceImpl implements BankCardService {
	
	private static final short CARD_LIFETIME = 4; // 4 years 

	private final BankCardRepository cardRepository;
	private final BankCardUtils cardUtils;
	private final PanEncryptor panEncryptor;
	private final DateFormatUtils dateFormatUtils; 
	private final SecurityUtils securityUtils;
	
	@Override
	public List<BankCard> getAll() {
		return cardRepository.findAll();
	}
	
	@Override
	public List<BankCard> getAllByOwnerId(Long id) {
		return cardRepository.findAllByOwnerId(id);
	}
	
	@Override
	public BankCard getById(Long cardId, Long userId) {
		BankCard card = cardRepository.findById(cardId).orElseThrow(
				() -> new CardNotFoudException("Card with id = %s not found".formatted(cardId))
		);
		if (!securityUtils.cardBelogsToUser(card, userId)) {
			throw new InsufficientRightsException("User can only view his own cards");
		}
		return card;
	}
	
	@Override
	public RevealedCardInfoDto revealCardInfo(Long cardId, Long userId, String password) {
		BankCard card = this.getById(cardId, userId);
		
		if (!securityUtils.passwordsMatch(card.getOwner(), password)) {
			throw new InsufficientRightsException("Incorrect password");
		}
		
		String pan = panEncryptor.decrypt(card.getEncryptedPan());
		String expirationDate = dateFormatUtils.format(card.getExpirationDate());
		
		return new RevealedCardInfoDto(pan, expirationDate);
	}
	
	@Override
	public BankCard save(BankCard card) {
		String pan = cardUtils.generatePan();
		
		card.setMaskedPan(cardUtils.getMaskedPan(pan));
		card.setEncryptedPan(panEncryptor.encrypt(pan));
		card.setExpirationDate(LocalDate.now().plusYears(CARD_LIFETIME));
		card.setStatus(CardStatus.ACTIVE);
		
		log.debug(card.toString());
		
		return cardRepository.save(card);
	}

	@Override
	public void updateStatus(Long id, CardStatus status) {
		BankCard card = cardRepository.findById(id).orElseThrow(
				() -> new CardNotFoudException("Card with id = %s not found".formatted(id))
		);
		card.setStatus(status);
		cardRepository.save(card);
	}

	@Override
	public void deleteById(Long id) {
		cardRepository.deleteById(id);
	}

}
