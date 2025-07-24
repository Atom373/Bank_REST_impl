package com.example.bankcards.controller.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CardCreateRequest(
		@JsonProperty("owner_email")
		String ownerEmail,
		
		String balance
) {}