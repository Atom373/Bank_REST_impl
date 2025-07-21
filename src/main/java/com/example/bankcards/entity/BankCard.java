package com.example.bankcards.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.bankcards.enums.CardStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "bank_cards")
public class BankCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String maskedPan;
	String encryptedPan;
	User owner;
	LocalDate expirationDate;
	CardStatus status;
	BigDecimal balance;
}
