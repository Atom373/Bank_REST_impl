package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevealedCardInfoDto {

	private String pan;
	
	private String expirationDate;
}