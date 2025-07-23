package com.example.bankcards.controller.payload;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
		@JsonProperty("from_card_pan")
		String fromCardPan,
		
		@JsonProperty("to_card_pan")
		String toCardPan,
		
		@DecimalMin(value = "1.00", inclusive = true, message = "Amount must be non-negative")
		@NotNull(message = "Transaction amount is required")
		BigDecimal amount
		
) {}