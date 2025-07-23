package com.example.bankcards.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
	
	private final PasswordEncoder passwordEncoder;

	public boolean cardBelogsToUser(BankCard card, Long userId) {
		return card.getOwner().getId() == userId;
	}
	
	public boolean passwordsMatch(User user, String password) {
		return passwordEncoder.matches(password, user.getPassword());
	}
}
