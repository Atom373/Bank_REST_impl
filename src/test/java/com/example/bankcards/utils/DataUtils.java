package com.example.bankcards.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.util.BankCardUtils;

public class DataUtils {
	
	private static final short CARD_LIFETIME = 4; // 4 years 
	
	private static long cardId = 0;
	
	private static BankCardUtils cardUtils = new BankCardUtils();
	
	public static User getAdmin() {
		User u = new User();
		u.setId(1L);
		u.setEmail("admin@gmail.com");
		u.setRole(UserRole.ROLE_ADMIN);
		return u;
	}
	
	public static User getUser() {
		User u = new User();
		u.setId(2L);
		u.setEmail("bob@gmail.com");
		u.setRole(UserRole.ROLE_USER);
		return u;
	}

	public static List<BankCard> getCardList() {
		List<BankCard> cards = new ArrayList<>();
		cards.add(getNewBankCard());
		cards.add(getNewBankCard());
		return cards;
	}
	
	public static BankCard getNewBankCard() {
		BankCard card = getCardForSaving();
		
		card.setId(++cardId);
		card.setStatus(CardStatus.ACTIVE);
		card.setExpirationDate(LocalDate.now().plusYears(CARD_LIFETIME));
		card.setEncryptedPan("encrypted" + cardId);
		
		return card;
	}
	
	public static BankCard getCardForSaving() {
		BankCard card = new BankCard();
		
		card.setOwner(getUser());
		card.setBalance(new BigDecimal("100.00"));
		
		return card;
	}
}
