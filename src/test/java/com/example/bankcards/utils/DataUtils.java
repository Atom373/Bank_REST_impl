package com.example.bankcards.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.util.CardUtils;

public class DataUtils {
	
	private static final short CARD_LIFETIME = 4; // 4 years 
	
	private static long cardId = 0;
	
	private static CardUtils cardUtils = new CardUtils();
	
	public static User getAdmin() {
		User u = new User();
		u.setId(1L);
		u.setEmail("admin@gmail.com");
		u.setPassword("encoded");
		u.setRole(UserRole.ROLE_ADMIN);
		return u;
	}
	
	public static User getUser() {
		User u = new User();
		u.setId(2L);
		u.setEmail("bob@gmail.com");
		u.setPassword("encoded");
		u.setRole(UserRole.ROLE_USER);
		return u;
	}
	
	public static User getUserForSaving() {
		User u = new User();
		u.setEmail("bob@gmail.com");
		u.setPassword("password");
		return u;
	}

	public static List<User> getUserList() {
		List<User> users = new ArrayList<>();
		users.add(getAdmin());
		users.add(getUser());
		return users;
	}
	
	public static List<Card> getCardList() {
		List<Card> cards = new ArrayList<>();
		cards.add(getNewCard());
		cards.add(getNewCard());
		return cards;
	}
	
	public static Card getNewCard() {
		Card card = getCardForSaving();
		
		card.setId(++cardId);
		card.setStatus(CardStatus.ACTIVE);
		card.setExpirationDate(LocalDate.now().plusYears(CARD_LIFETIME));
		card.setEncryptedPan("encrypted" + cardId);
		
		return card;
	}
	
	public static Card getCardForSaving() {
		Card card = new Card();
		
		card.setOwner(getUser());
		card.setBalance(new BigDecimal("100.00"));
		
		return card;
	}

}
