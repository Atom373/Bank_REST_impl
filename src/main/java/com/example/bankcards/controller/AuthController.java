package com.example.bankcards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankcards.controller.payload.AuthRequest;
import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.service.AuthenticationServiceFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthenticationServiceFacade authenticationService;
	
	@GetMapping("/test")
	public String test(@AuthenticationPrincipal Long userId) {
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		return "Secured test message for user(id=%s)".formatted(userId);
	}
	
	@PostMapping
	public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
		String jwt = authenticationService.authenticate(request);
		return ResponseEntity.ok()
				.body(new AuthResponse(jwt));
	}
}
