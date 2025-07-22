package com.example.bankcards.dto;

import com.example.bankcards.enums.CardStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BankCardDto {

	private Long id;
	
	@JsonProperty("masked_pan")
	private String maskedPan;
	
	@JsonProperty("owner_email")
	private String ownerEmail;
	
	private CardStatus status;
	
	private String balance;
}
