package com.example.bankcards.controller.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BankCardCreateRequest(
		@JsonProperty("owner_email")
		String ownerEmail,
		
		String balance
) {}