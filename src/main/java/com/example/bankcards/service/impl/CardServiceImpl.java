package com.example.bankcards.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankcards.controller.payload.TransactionRequest;
import com.example.bankcards.dto.RevealedCardInfoDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.CardBlockedOrExpiredException;
import com.example.bankcards.exception.CardNotFoudException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.InsufficientRightsException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardUtils;
import com.example.bankcards.util.DateFormatUtils;
import com.example.bankcards.util.PanEncryptor;
import com.example.bankcards.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
	
	private static final short CARD_LIFETIME = 4; // 4 years 

	private final CardRepository cardRepository;
	private final CardUtils cardUtils;
	private final PanEncryptor panEncryptor;
	private final DateFormatUtils dateFormatUtils; 
	private final SecurityUtils securityUtils;
	
	@Override
	public List<Card> getAll() {
		return cardRepository.findAll();
	}
	
	@Override
	public List<Card> getAllByOwnerId(Long id) {
		return cardRepository.findAllByOwnerId(id);
	}
	
	@Override
	public Page<Card> getAllByOwnerId(Long id, Pageable pageable) {
		return cardRepository.findAllByOwnerId(id, pageable);
	}
	
	@Override
	public Card getById(Long cardId, Long userId) {
		Card card = cardRepository.findById(cardId).orElseThrow(
				() -> new CardNotFoudException("Card with id = %s not found".formatted(cardId))
		);
		if (!securityUtils.cardBelogsToUser(card, userId)) {
			throw new InsufficientRightsException("User can only view his own cards");
		}
		return card;
	}
	
	@Override
	public RevealedCardInfoDto revealCardInfo(Long cardId, Long userId, String password) {
		log.info("User(id=%s) requested full card info for card(id=%s)".formatted(userId, cardId));
		
		Card card = this.getById(cardId, userId);
		
		if (!securityUtils.passwordsMatch(card.getOwner(), password)) {
			throw new InsufficientRightsException("Incorrect password");
		}
		
		String pan = panEncryptor.decrypt(card.getEncryptedPan());
		String expirationDate = dateFormatUtils.format(card.getExpirationDate());
		
		log.info("User(id=%s) got access to full card info for card(id=%s)".formatted(userId, cardId));
		
		return new RevealedCardInfoDto(pan, expirationDate);
	}
	
	@Override
	public Card save(Card card) {
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
		Card card = cardRepository.findById(id).orElseThrow(
				() -> new CardNotFoudException("Card with id = %s not found".formatted(id))
		);
		card.setStatus(status);
		cardRepository.save(card);
	}
	
	@Override
	@Transactional
	public void transfer(TransactionRequest request, Long userId) {	
		Card fromCard = this.getByPan(request.fromCardPan());
		Card toCard = this.getByPan(request.toCardPan());
		
		if (fromCard.getStatus() != CardStatus.ACTIVE)
			throw new CardBlockedOrExpiredException(
					"User is trying to transfer money from a blocked or expired card"
			);
		
		if (toCard.getStatus() != CardStatus.ACTIVE)
			throw new CardBlockedOrExpiredException(
					"User is trying to transfer money to a blocked or expired card"
			);
		
		if (!(securityUtils.cardBelogsToUser(fromCard, userId)
			&& securityUtils.cardBelogsToUser(toCard, userId)))
			throw new InsufficientRightsException("User can only do transfers between his own cards");
		
		BigDecimal fromCardBalance = fromCard.getBalance();
		BigDecimal toCardBalance = toCard.getBalance();
		
		if (fromCardBalance.compareTo(request.amount()) < 0)
			throw new InsufficientFundsException("Not enough funds for the transfer");
		
		fromCard.setBalance(fromCardBalance.subtract(request.amount()));
		toCard.setBalance(toCardBalance.add(request.amount()));
		
		cardRepository.save(fromCard);
		cardRepository.save(toCard);
		
		log.info("Transaction completed. From pan = %s to pan = %s with amount = %s"
				.formatted(fromCard.getMaskedPan(), toCard.getMaskedPan(), request.amount()));
	}

	@Override
	public void deleteById(Long id) {
		cardRepository.deleteById(id);
	}

	private Card getByPan(String pan) {
		String encryptedPan = panEncryptor.encrypt(pan);
		return cardRepository.findByEncryptedPan(encryptedPan).orElseThrow(
				() -> new CardNotFoudException("Card with provided pan not found")
		);
	}
}
