package com.example.bankcards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.bankcards.controller.payload.TransactionRequest;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.CardNotFoudException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.InsufficientRightsException;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.service.impl.BankCardServiceImpl;
import com.example.bankcards.util.BankCardUtils;
import com.example.bankcards.util.DateFormatUtils;
import com.example.bankcards.util.PanEncryptor;
import com.example.bankcards.util.SecurityUtils;
import com.example.bankcards.utils.DataUtils;

@ExtendWith(MockitoExtension.class)
public class BankCardServiceImplTest {

	@Spy
	private BankCardUtils cardUtils = new BankCardUtils();
	
	@Spy
	private DateFormatUtils dateFormatUtils = new DateFormatUtils(); 
	
	@Mock
	private BankCardRepository cardRepository;
	
	@Mock
	private PanEncryptor panEncryptor;
	
	@Mock
	private SecurityUtils securityUtils;
	
	@InjectMocks
	private BankCardServiceImpl bankCardService; 
	
	@Test
	public void save_shouldSaveGivenCardEntity() {
		// given
		BankCard card = DataUtils.getNewBankCard(); 
		when(cardRepository.save(any(BankCard.class))).thenReturn(card);
		when(panEncryptor.encrypt(anyString())).thenReturn(card.getEncryptedPan());
		
		// when
		BankCard savedCard = bankCardService.save(DataUtils.getCardForSaving());
		
		// then
		assertEquals(card, savedCard);
		verify(panEncryptor, times(1)).encrypt(anyString());
		verify(cardUtils, times(1)).generatePan();
	}
	
	@Test
	public void getById_shouldReturnEntityById() {
		// given
		BankCard card = DataUtils.getNewBankCard();
		when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
		when(securityUtils.cardBelogsToUser(card, card.getOwner().getId())).thenReturn(true);
		
		// when
		BankCard retrievedCard = bankCardService.getById(card.getId(), card.getOwner().getId());
		
		// then
		assertEquals(card, retrievedCard);
	}
	
	@Test
	public void getById_entityDoesNotExist_shouldThrowException() {
		// given
		when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		// then
		assertThrows(CardNotFoudException.class, () -> bankCardService.getById(999L, null));
	}
	
	@Test
	public void getById_cardDoesNotBelongToUser_shouldThrowException() {
		// given
		BankCard card = DataUtils.getNewBankCard();
		when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
		when(securityUtils.cardBelogsToUser(any(BankCard.class), anyLong())).thenReturn(false);
		
		// then
		assertThrows(
				InsufficientRightsException.class, 
				() -> bankCardService.getById(card.getId(), 999L)
		);
	}
	
	@Test
	public void transfer_shouldTransferMoneyFromOneCardToAnother() {
		// given
		BankCard fromCard = DataUtils.getNewBankCard();
		BankCard toCard = DataUtils.getNewBankCard();
		
		String fromCardPan = fromCard.getEncryptedPan();
		String toCardPan = toCard.getEncryptedPan();
		
		when(panEncryptor.encrypt(fromCardPan)).thenReturn(fromCardPan);
		when(panEncryptor.encrypt(toCardPan)).thenReturn(toCardPan);
		when(securityUtils.cardBelogsToUser(any(BankCard.class), anyLong())).thenReturn(true);
		when(cardRepository.findByEncryptedPan(fromCardPan)).thenReturn(Optional.of(fromCard));
		when(cardRepository.findByEncryptedPan(toCardPan)).thenReturn(Optional.of(toCard));
		when(cardRepository.save(any(BankCard.class))).thenReturn(null);
		
		TransactionRequest req = new TransactionRequest(
				fromCardPan,
				toCardPan,
				new BigDecimal("50.00")
		);
		
		// when
		bankCardService.transfer(req, fromCard.getOwner().getId());
		
		// then
		assertEquals(fromCard.getBalance().toString(), "50.00");
		assertEquals(toCard.getBalance().toString(), "150.00");
	}
	
	@Test
	public void transfer_notEnoughFunds_shouldThrowException() {
		// given
		BankCard fromCard = DataUtils.getNewBankCard();
		BankCard toCard = DataUtils.getNewBankCard();
		
		String fromCardPan = fromCard.getEncryptedPan();
		String toCardPan = toCard.getEncryptedPan();
		
		when(panEncryptor.encrypt(fromCardPan)).thenReturn(fromCardPan);
		when(panEncryptor.encrypt(toCardPan)).thenReturn(toCardPan);
		when(securityUtils.cardBelogsToUser(any(BankCard.class), anyLong())).thenReturn(true);
		when(cardRepository.findByEncryptedPan(fromCardPan)).thenReturn(Optional.of(fromCard));
		when(cardRepository.findByEncryptedPan(toCardPan)).thenReturn(Optional.of(toCard));

		TransactionRequest req = new TransactionRequest(
				fromCardPan,
				toCardPan,
				new BigDecimal("999.00")
		);
		
		// then
		assertThrows(
				InsufficientFundsException.class,
				() -> bankCardService.transfer(req, fromCard.getOwner().getId()));
	}
	
	@Test
	public void updateStatus_should() {
		// given
		BankCard card = DataUtils.getNewBankCard();
		when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
		when(cardRepository.save(any(BankCard.class))).thenReturn(null);
		
		// when
		bankCardService.updateStatus(card.getId(), CardStatus.BLOCKED);
		
		// then
		verify(cardRepository, times(1)).findById(card.getId());
		verify(cardRepository, times(1)).save(any(BankCard.class));
	}
}
