package com.example.bankcards.service;

import com.example.bankcards.controller.payload.AuthRequest;

public interface AuthenticationServiceFacade {

	public String authenticate(AuthRequest request);
	
}
