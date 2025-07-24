package com.example.bankcards.dto;

import com.example.bankcards.enums.CardStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DetailedCardDto {

	private Long id;
	
	@JsonProperty("masked_pan")
	private String maskedPan;
	
	private CardStatus status;
	
	private String balance;
}
