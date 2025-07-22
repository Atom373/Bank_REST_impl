package com.example.bankcards.controller.payload;

public record AuthRequest(
		String email,
		String password
) {}