package com.example.bankcards.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.bankcards.enums.CardStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor()
@Table(name = "bank_cards")
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String maskedPan;
	
	private String encryptedPan;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;
	
	private LocalDate expirationDate;
	
	@Enumerated(EnumType.STRING)
	private CardStatus status;
	
	private BigDecimal balance;
}
